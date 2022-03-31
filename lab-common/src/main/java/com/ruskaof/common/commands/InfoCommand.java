package com.ruskaof.common.commands;

import com.ruskaof.common.dto.CommandResultDto;
import com.ruskaof.common.util.CollectionManager;
import com.ruskaof.common.util.HistoryManager;

import java.io.Serializable;
import java.time.LocalDate;


public class InfoCommand extends Command {

    public InfoCommand() {
        super("", "info");
    }

    @Override
    public CommandResultDto execute(
            CollectionManager collectionManager,
            HistoryManager historyManager
    ) {
        // Stream api would not help
        if (!collectionManager.getMainData().isEmpty()) {
            return new CommandResultDto(new InfoCommandResult(collectionManager.getMainData().size(),
                    collectionManager.getCreationDate(),
                    collectionManager.getMainData().first().getStudentsCount())
            );
        } else {
            return new CommandResultDto(new InfoCommandResult(collectionManager.getMainData().size(),
                    collectionManager.getCreationDate(),
                    0)
            );
        }
    }

    final class InfoCommandResult implements Serializable {
        private final int numberOfElements;
        private final LocalDate creationDate;
        private final int biggestStudentsCount;

        private InfoCommandResult(int numberOfElements, LocalDate creationDate, int biggestStudentsCount) {
            this.numberOfElements = numberOfElements;
            this.creationDate = creationDate;
            this.biggestStudentsCount = biggestStudentsCount;
        }

        @Override
        public String toString() {
            return "InfoCommandResult{"
                    + "numberOfElements='" + numberOfElements + '\''
                    + ", creationDate=" + creationDate
                    + ", biggestStudentsCount=" + biggestStudentsCount
                    + '}';
        }
    }
}
