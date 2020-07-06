package me.kenchh.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.kenchh.checks.Check;
import me.kenchh.checks.CheckManager;
import org.bukkit.entity.Player;

public class PacketHandler extends ChannelDuplexHandler {
    private Player p;

    public PacketHandler(final Player p) {
        this.p = p;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext c, Object m) throws Exception {
        super.channelRead(c, m);
        for(Check check : CheckManager.checks) {
            if(check instanceof PacketListener) {
                ((PacketListener) check).readPacket(p, m);
            }
        }
    }

}