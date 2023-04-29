package com.driver;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }
        userMobile.add(mobile);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users) {
        Group group=null;
            if(users.size()==2){
                String name=users.get(1).getName();
                group= new Group(name,2);
            }
            else{
                customGroupCount++;
                String name="Group "+customGroupCount;
                group= new Group(name,users.size());
               groupUserMap.put(group,users);
            }
        return group;
    }

    public int createMessage(String content) {
        messageId++;
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy.HH:mm:ss");
        Date date=new Date();
        Message message= new Message(messageId,content,date);
        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        else {
            List<User> listUsers= groupUserMap.get(group);
            if(!listUsers.contains(sender)){
                throw  new Exception("You are not allowed to send message");
            }
            else {
                List<Message> listMessage=new ArrayList<>();
                if(groupMessageMap.containsKey(group)){
                    listMessage= groupMessageMap.get(group);
                }
                listMessage.add(message);
                groupMessageMap.put(group,listMessage);
            }
        }
        return groupMessageMap.get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }
        else {
            List<User> listUsers= groupUserMap.get(group);
            if(!listUsers.get(0).equals(approver)){
                throw  new Exception("Approver does not have rights");
            }
            if(!listUsers.contains(user)){
                throw  new Exception("User is not a participant");
            }
            User newAdmin=user;
            listUsers.remove(approver);
            listUsers.remove(user);
            listUsers.add(0,newAdmin);
            listUsers.add(approver);
    }
        return "SUCCESS";
    }

    public String findMessage(Date start, Date end, int k) {
        return "success";
    }

    public int removeUser(User user) {
        return messageId+customGroupCount;
    }
}
