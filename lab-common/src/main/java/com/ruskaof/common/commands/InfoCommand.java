package com.ruskaof.common.commands;

import com.ruskaof.common.dto.CommandResultDto;
import com.ruskaof.common.util.CollectionManager;
import com.ruskaof.common.util.HistoryManager;

import java.io.Serializable;


public class InfoCommand extends Command {

    public InfoCommand() {
        super("info");
    }

    @Override
    public CommandResultDto execute(
            CollectionManager collectionManager,
            HistoryManager historyManager,
            String username
    ) {
        historyManager.addNote(this.getName());

        return new CommandResultDto(collectionManager.getInfoAboutCollections());
    }

    public static final class InfoCommandResult implements Serializable {
        private final int numberOfElements;
        private final int biggestStudentsCount;

        public InfoCommandResult(Integer numberOfElements, int biggestStudentsCount) {
            this.numberOfElements = numberOfElements;
            this.biggestStudentsCount = biggestStudentsCount;
        }

        @Override
        public String toString() {
            return "InfoCommandResult{"
                    + "numberOfElements='" + numberOfElements + '\''
                    + ", biggestStudentsCount=" + biggestStudentsCount
                    + '}';
        }
    }
}
