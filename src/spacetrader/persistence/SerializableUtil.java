/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spacetrader.persistence;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 *
 * @author nkaru_000
 */
public class SerializableUtil {

    
    public static void serialize(Object object, String fileName) throws
            FileNotFoundException, IOException {
        OutputStream fileOutput = new FileOutputStream(fileName);
        OutputStream buffer = new BufferedOutputStream(fileOutput);
        ObjectOutput objOutput = new ObjectOutputStream(buffer);
        objOutput.writeObject(object);
        objOutput.close();
        fileOutput.close();
    }

    public static Object deserialize(String fileName) throws
            IOException, ClassNotFoundException {
        InputStream fileInput = new FileInputStream(fileName);
        InputStream buffer = new BufferedInputStream(fileInput);
        ObjectInput objInput = new ObjectInputStream(buffer);
        Object o = objInput.readObject();
        objInput.close();
        fileInput.close();
        return o;
    }
}
