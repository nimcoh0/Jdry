package org.softauto.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * observer for listeners impl classes
 */
public class ListenerObserver {

    private static final org.softauto.logger.Logger logger = org.softauto.logger.LogManager.getLogger(ListenerObserver.class);
    private List<HashMap<String,Object>> channels = null;

    private static ListenerObserver listenerObserver = null;

    public static ListenerObserver getInstance(){
        if(listenerObserver == null){
            listenerObserver =  new ListenerObserver();
        }
        return listenerObserver;
    }

    /**
     * clear the channels list
     */
    public void reset(){
        HashMap<String,Object> channel = channels.get(0);
        channels = new ArrayList<>();
        channels.add(channel);
    }

    private ListenerObserver(){
        channels = new ArrayList<>();
    }

    /**
     * add new listener impl class
     * @param key
     * @param value
     */
    public void register(String key,Object value){
        try {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put(key, value);
                channels.add(hm);
                logger.debug("ListenerObserver register " + key);
        }catch (Exception e){
            logger.error("fail to register listener impl class "+ key);
        }
    }


    /**
     * get channel
     * @param key
     * @return
     */
    public Object getChannel(String key) {
        try {
            for (HashMap<String, Object> chanel : channels) {
                if (chanel.containsKey(key)) {
                    return chanel.get(key);
                }
            }
            logger.debug("observer channel for " + key + " fail ");
        }catch (Exception e){
            logger.error(" fail to get channel by "+ key,e);
        }
        logger.debug("observer channel for " + key + " not found  ");
        return null;
    }

    /**
     * get all channels by key
     * @param key
     * @return
     */
    public  List<Object> getChannels(String key) {
        List<Object> l = new ArrayList<>();
        try {
            for (HashMap<String, Object> chanel : channels) {
                if (chanel.containsKey(key)) {
                    l.add(chanel.get(key));
                }
            }
            logger.debug("observer channel for " + key + " found " + l.size());
        }catch (Exception e){
            logger.error("fail to get observer channel for " + key );
        }
        return l;
    }

    /**
     * get all channels
     * @return
     */
    public  List<Object> getChannels() {
        List<Object> l = new ArrayList<>();
        try {
            for (HashMap<String, Object> chanel : channels) {
                chanel.forEach((k, v) -> {
                    l.add(v);
                });
            }
            logger.debug("observer channels for  found " + l.size());
        }catch (Exception e){
            logger.error("fail to get observer channels  "  );
        }
        return l;
    }

}