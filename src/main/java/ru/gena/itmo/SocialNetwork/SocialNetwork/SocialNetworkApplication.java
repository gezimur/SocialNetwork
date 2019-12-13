package ru.gena.itmo.SocialNetwork.SocialNetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SocialNetworkApplication{

	public static void main(String[] args) {
		String message = new Preparer().preparer();
		if (message != null){
			SpringApplication.run(SocialNetworkApplication.class, args);
		}else{
			System.out.println(message);
		}
	}
}
