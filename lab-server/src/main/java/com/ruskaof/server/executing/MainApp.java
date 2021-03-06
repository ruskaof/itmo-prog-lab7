package com.ruskaof.server.executing;

import com.ruskaof.common.dto.CommandFromClientDto;
import com.ruskaof.common.dto.CommandResultDto;
import com.ruskaof.common.util.DataManager;
import com.ruskaof.common.util.Pair;
import com.ruskaof.common.util.State;
import com.ruskaof.server.connection.ClientDataReceiver;
import com.ruskaof.server.connection.ClientDataSender;
import com.ruskaof.server.util.HistoryManagerImpl;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

public class MainApp {
    private final Queue<Pair<CommandFromClientDto, SocketAddress>> queueToBeExecuted;
    private final Queue<Pair<CommandResultDto, SocketAddress>> queueToBeSent;
    private final Logger logger;
    private final int port;
    private final String ip;
    private final CommandHandler commandHandler;
    private final ClientDataReceiver clientDataReceiver;
    private final ExecutorService threadPool;
    private final ForkJoinPool forkJoinPool;
    private final DataManager dataManager;

    public MainApp(
            Logger logger,
            int port,
            String ip,
            ExecutorService threadPool,
            ForkJoinPool forkJoinPool,
            DataManager dataManager
    ) {
        this.logger = logger;
        this.ip = ip;
        this.port = port;
        queueToBeExecuted = new LinkedBlockingQueue<>();
        queueToBeSent = new LinkedBlockingQueue<>();
        this.commandHandler = new CommandHandler(queueToBeExecuted, queueToBeSent, logger, dataManager, new HistoryManagerImpl());
        this.clientDataReceiver = new ClientDataReceiver(logger, queueToBeExecuted);
        this.threadPool = threadPool;
        this.forkJoinPool = forkJoinPool;
        this.dataManager = dataManager;
    }

    public void start(
            State<Boolean> isWorking
    ) throws IOException {
        try (DatagramChannel datagramChannel = DatagramChannel.open()) {
            datagramChannel.bind(new InetSocketAddress(ip, port));
            datagramChannel.configureBlocking(false);

            threadPool.submit(() -> {
                try {
                    clientDataReceiver.startReceivingData(datagramChannel, isWorking, threadPool);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });


            commandHandler.startToHandleCommands(
                    isWorking,
                    threadPool
            );


            while (isWorking.getValue()) {
                if (!queueToBeSent.isEmpty()) {
                    Pair<CommandResultDto, SocketAddress> commandResultDtoAndSocketAddress = queueToBeSent.poll();
                    forkJoinPool.invoke(new ClientDataSender(commandResultDtoAndSocketAddress.getFirst(), datagramChannel, commandResultDtoAndSocketAddress.getSecond(), logger));
                }
            }

        } catch (BindException e) {
            logger.error("Could not start the server, bind exception. Please, use another port.");
            isWorking.setValue(false);
        }
    }
}
