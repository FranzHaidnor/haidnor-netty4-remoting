package haidnor.remoting.core;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.FileRegion;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.ssl.SslHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

/**
 * <p>
 * By default, file region are directly transferred to socket channel which is known as zero copy. In case we need
 * to encrypt transmission, data being sent should go through the {@link SslHandler}. This encoder ensures this
 * process.
 * </p>
 */
public class FileRegionEncoder extends MessageToByteEncoder<FileRegion> {

    /**
     * Encode a message into a {@link ByteBuf}. This method will be called for each written message that
     * can be handled by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link
     *            MessageToByteEncoder} belongs to
     * @param msg the message to encode
     * @param out the {@link ByteBuf} into which the encoded message will be written
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, FileRegion msg, final ByteBuf out) throws Exception {
        WritableByteChannel writableByteChannel = new WritableByteChannel() {
            @Override
            public int write(ByteBuffer src) throws IOException {
                out.writeBytes(src);
                return out.capacity();
            }

            @Override
            public boolean isOpen() {
                return true;
            }

            @Override
            public void close() throws IOException {
            }
        };

        long toTransfer = msg.count();

        while (true) {
            long transferred = msg.transfered();
            if (toTransfer - transferred <= 0) {
                break;
            }
            msg.transferTo(writableByteChannel, transferred);
        }
    }
}