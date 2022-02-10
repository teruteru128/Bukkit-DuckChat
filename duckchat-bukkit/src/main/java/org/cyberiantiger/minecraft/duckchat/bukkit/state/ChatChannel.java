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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.jgroups.Address;
import org.jgroups.util.ByteArrayDataInputStream;
import org.jgroups.util.ByteArrayDataOutputStream;
import org.jgroups.util.Util;

/**
 *
 * @author antony
 */
public class ChatChannel implements Serializable {
    public static final int FLAG_LOCAL_AUTO_JOIN = 0;
    public static final int FLAG_GLOBAL_AUTO_JOIN = 1;

    private Address owner;
    private String name;
    private Map<String, Member> members = new HashMap<String, Member>();
    private ChatChannelMetadata metadata;

    public ChatChannel(Address owner, String name, ChatChannelMetadata metadata) {
        this.owner = owner;
        this.name = name;
        this.metadata = metadata;
    }

    public Address getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }


    public boolean isAutoJoin(Member member) {
        BitSet flags = metadata.getFlags();
        return (member.getAddress().equals(owner) && flags.get(FLAG_LOCAL_AUTO_JOIN)) ||
                flags.get(FLAG_GLOBAL_AUTO_JOIN);
    }

    public void addMember(Member member) {
        members.put(member.getIdentifier(), member);
    }

    public void removeMember(String identifier) {
        members.remove(identifier);
    }

    public Collection<Member> getMembers() {
        return members.values();
    }


    boolean isMember(String identifier) {
        return members.containsKey(identifier);
    }

    public ChatChannelMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ChatChannelMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "ChatChannel{" + "owner=" + owner + ", name=" + name + ", members=" + members + ", metadata=" + metadata + '}';
    }

    private void writeObject(ObjectOutputStream out)
            throws IOException {
        ByteArrayDataOutputStream dos = new ByteArrayDataOutputStream();
        Util.writeAddress(owner, dos);
        byte[] buffer =dos.buffer();
        out.writeInt(buffer.length);
        out.write(buffer);
        out.writeObject(name);
        out.writeObject(members);
        out.writeObject(metadata);
    }

    private void readObject(ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        int len = in.readInt();
        byte[] buffer = new byte[len];
        in.read(buffer);
        owner = Util.readAddress(new ByteArrayDataInputStream(buffer));
        name = (String)in.readObject();
        members = (Map<String, Member>)in.readObject();
        metadata = (ChatChannelMetadata)in.readObject();
    }
}
