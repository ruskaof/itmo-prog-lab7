package com.ruskaof.common.commands;

import com.ruskaof.common.dto.CommandResultDto;
import com.ruskaof.common.util.CollectionManager;
import com.ruskaof.common.util.HistoryManager;


public class ShowCommand extends Command {


    public ShowCommand() {
        super("", "show");
    }

    @Override
    public CommandResultDto execute(
            CollectionManager collectionManager,
            HistoryManager historyManager
    ) {
        // Stream api would not help
        return new CommandResultDto(collectionManager.getMainData());
    }
}
