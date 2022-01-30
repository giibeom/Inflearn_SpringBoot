package hello.core;

import jdk.jfr.SettingDefinition;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Created by 명기범 on 2022-01-30
 */
@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("asdfas");

        String name = helloLombok.getName();
        System.out.println("name = " + name);
        System.out.println("helloLombok = " + helloLombok);
    }
}
