/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.duckchat.bukkit.state;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.BitSet;
import org.jgroups.Address;
import org.jgroups.util.ByteArrayDataInputStream;
import org.jgroups.util.ByteArrayDataOutputStream;
import org.jgroups.util.Util;

/**
 *
 * @author antony
 */
public class Member implements Serializable {

    private Address address;
    private String identifier;
    private String name;
    private BitSet flags;

    public Member(Address address, String identifier, String name, BitSet flags) {
        this.address = address;
        this.identifier = identifier;
        this.name = name;
        this.flags = flags;
    }

    public Address getAddress() {
        return address;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public BitSet getFlags() {
        return flags;
    }

    public void setFlags(BitSet flags) {
        this.flags = flags;
    }

    @Override
    public String toString() {
        return "Member{" + "address=" + address + ", identifier=" + identifier + ", name=" + name + ", flags=" + flags + '}';
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException {
        ByteArrayDataOutputStream dos = new ByteArrayDataOutputStream();
        Util.writeObject(address, dos);
        byte[] buffer =dos.buffer();
        out.writeInt(buffer.length);
        out.write(buffer);
        out.writeObject(identifier);
        out.writeObject(name);
        out.writeObject(flags);
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        int len = in.readInt();

        byte[] buffer = new byte[len];
        in.read(buffer);
        address = (Address)Util.readObject(new ByteArrayDataInputStream(buffer));
        identifier = (String)in.readObject();
        name = (String)in.readObject();
        flags = (BitSet)in.readObject();
    }
}
