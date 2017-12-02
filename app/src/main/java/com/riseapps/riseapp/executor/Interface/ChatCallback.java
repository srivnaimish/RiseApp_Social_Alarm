package com.riseapps.riseapp.executor.Interface;

import com.riseapps.riseapp.model.DB.Chat_Entity;
import com.riseapps.riseapp.model.DB.ChatSummary;

import java.util.ArrayList;

/**
 * Created by naimish on 2/12/17.
 */

public interface ChatCallback {
    public void summariesFetched(ArrayList<ChatSummary> chatSummaries);
    public void chatFetched(ArrayList<Chat_Entity> chatList);
}
