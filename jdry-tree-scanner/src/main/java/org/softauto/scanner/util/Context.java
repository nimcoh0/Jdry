package org.softauto.scanner.util;





import org.apache.avro.Item;

import java.util.HashMap;
import java.util.LinkedList;


public class Context {

    HashMap<String,Object> hm = new HashMap<>();



    public Object get(String name){
        return hm.get(name);
    }

    public void put(String key,Object value){
        hm.put(key,value);
    }


    HashMap<String , Item> keyToItem = new HashMap<>();

    LinkedList<HashMap<Integer, Item>> itemList = new LinkedList<>();

    public LinkedList<HashMap<Integer, Item>> getItemList(){
        return itemList;
    }

    public void addKeyToItem(String key,Item item){
        keyToItem.put(key,item);
    }

    public Item getItemByKey(String key){
        return keyToItem.get(key);
    }

    public void addItem(Integer orderId, Item item){
        HashMap<Integer, Item> hm = new HashMap<>();
        hm.put(orderId,item);
        itemList.add(hm);
    }

    public HashMap<Integer, Item> getLast(){
        return itemList.getLast();
    }

    public HashMap<Integer, Item> getFirst(){
        return itemList.getFirst();
    }


    public int size(){
        return itemList.size();
    }

    public HashMap<Integer, Item> get(int id){
        if(id > -1 && id <= itemList.size()) {
            return itemList.get(id);
        }
        return null;
    }

    public Item getItem(int id){
        if(id > -1 && id <= itemList.size()) {
            return (Item) itemList.get(id).values().toArray()[0];
        }
        return null;
    }

    public Integer getItemOrderId(int id){
        if(id > -1 && id <= itemList.size()) {
            return (Integer) itemList.get(id).keySet().toArray()[0];
        }
        return null;
    }

    public int getId(HashMap<Integer, Item> item){
        return itemList.indexOf(item);
    }


}
