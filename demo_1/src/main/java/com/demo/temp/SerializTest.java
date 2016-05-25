package com.demo.temp;

import com.demo.model.SerializeModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by 程祥 on 16/4/22.
 * Function：序列化学习demo
 */
public class SerializTest {

    public static void main(String[] args){

        SerializeModel model = new SerializeModel();
        model.setName("姓名~aaa");
        model.setAddress("住址~~~ 住址 address");
        model.setTempField("temp transient field");
        model.setUnTempField("temp un transient field");
        String basePath = System.getProperty("user.dir");
        Path path = Paths.get(basePath+"/demo_1/src/main/java/com/demo/temp/test.txt");
        try {
            ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(path));
            output.writeObject(model);
            output.close();
            ObjectInputStream input = new ObjectInputStream(Files.newInputStream(path));
            SerializeModel readModel = (SerializeModel)input.readObject();
            input.close();
            System.out.println(readModel.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


}
