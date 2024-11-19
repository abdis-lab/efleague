package com.abdisalam.efleague;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLOutput;
import java.util.Scanner;

@SpringBootApplication
public class EfleagueApplication {

    public static void main(String[] args) {
        SpringApplication.run(EfleagueApplication.class, args);


//        String network = "";
//
//        System.out.println("Initial Network: " + network);
//
//
//
//        network = addUser(network, "aagate", "abdi-kadir-james");
//        network = addUser(network, "nehaa24", "lebron-jordan");
//        network = addUser(network, "goat", "");
//
//
//        System.out.println("network After adding aagate and nehaa24");
//        System.out.println(network);
//
//
//        System.out.println("Network Size: " + networkSize(network));
//
//
//        System.out.println("Network After Removing: aagate");
//
//        network = removeUser(network, "aagate");
//        System.out.println(network);
//        System.out.println("Network Size: " + networkSize(network));

    }


//    public static String addUser(String network, String user, String friends){
//        if(network.contains(user + ":")){
//            return network;
//        }
//
//        if(friends == null || friends.isEmpty()){
//            friends = "";
//        }
//
//        return network + user + ":" + friends + ",";
//    }
//
//
//    public static int networkSize(String network){
//        int count = 0;
//
//        for(int i = 0; i < network.length(); i++){
//            if(network.charAt(i) == ':'){
//                count += 1;
//            }
//        }
//
//        return count;
//    }
//
//
//    public static String removeUser(String network, String user){
//        int startPosition = network.indexOf(user + ":");
//
//        if(startPosition == -1){
//            return network;
//        }
//
//        int endPosition = network.indexOf(",", startPosition);
//
//        String updatedNetwork = network.substring(0, startPosition) + network.substring(endPosition + 1);
//
//        return updatedNetwork;
//    }
//




}
