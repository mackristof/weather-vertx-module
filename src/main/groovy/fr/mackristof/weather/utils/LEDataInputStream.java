package fr.mackristof.weather.utils;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by christophe mourette on 28/06/14 for Geomatys.
 */

public class LEDataInputStream extends InputStream implements DataInput {
    final DataInputStream ds;
    final java.io.InputStream in;
    final byte buffer[] = new byte[8];

    public LEDataInputStream(final InputStream in) {
        this.in = in;
        this.ds = new DataInputStream(in);
    }

    @Override
    public int available() throws IOException {
        return ds.available();
    }

    @Override
    public final short readShort() throws IOException {
        ds.readFully(buffer, 0, 2);
        return (short) ((buffer[1] & 0xff) << 8
                | (buffer[0] & 0xff));
    }

    @Override
    public final int readUnsignedShort() throws IOException {
        ds.readFully(buffer, 0, 2);
        return ((buffer[1] & 0xff) << 8 | (buffer[0] & 0xff));
    }

    @Override
    public final char readChar() throws IOException {
        ds.readFully(buffer, 0, 2);
        return (char) ((buffer[1] & 0xff) << 8
                | (buffer[0] & 0xff));
    }

    @Override
    public final int readInt() throws IOException {
        ds.readFully(buffer, 0, 4);
        return (buffer[3]) << 24
                | (buffer[2] & 0xff) << 16
                | (buffer[1] & 0xff) << 8
                | (buffer[0] & 0xff);
    }

    @Override
    public final long readLong() throws IOException {
        ds.readFully(buffer, 0, 8);
        return (long) (buffer[7]) << 56
                | (long) (buffer[6] & 0xff) << 48
                | (long) (buffer[5] & 0xff) << 40
                | (long) (buffer[4] & 0xff) << 32
                | (long) (buffer[3] & 0xff) << 24
                | (long) (buffer[2] & 0xff) << 16
                | (long) (buffer[1] & 0xff) << 8
                | (long) (buffer[0] & 0xff);
    }

    @Override
    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public final int read(byte b[], int off, int len) throws IOException {
        return in.read(b, off, len);
    }

    @Override
    public final void readFully(byte b[]) throws IOException {
        ds.readFully(b, 0, b.length);
    }

    @Override
    public final void readFully(byte b[], int off, int len) throws IOException {
        ds.readFully(b, off, len);
    }

    @Override
    public final int skipBytes(int n) throws IOException {
        return ds.skipBytes(n);
    }

    @Override
    public final boolean readBoolean() throws IOException {
        return ds.readBoolean();
    }

    @Override
    public final byte readByte() throws IOException {
        return ds.readByte();
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public final int readUnsignedByte() throws IOException {
        return ds.readUnsignedByte();
    }

    @Override
    public final String readLine() throws IOException {
        return ds.readLine();
    }

    @Override
    public final String readUTF() throws IOException {
        return ds.readUTF();
    }

    @Override
    public final void close() throws IOException {
        ds.close();
    }


    public static short readUnsignedByte(final byte[] buffer, final int offset){
        return (short) (buffer[offset] & 0xff);
    }

    public static short readShort(final byte[] buffer, final int offset){
        return (short) ((buffer[offset+1] & 0xff) << 8
                | (buffer[offset+0] & 0xff));
    }

    public static int readUnsignedShort(final byte[] buffer, final int offset){
        return ((buffer[offset+1] & 0xff) << 8 | (buffer[offset+0] & 0xff));
    }

    public static char readChar(final byte[] buffer, final int offset){
        return (char) ((buffer[offset+1] & 0xff) << 8
                | (buffer[offset+0] & 0xff));
    }

    public static int readInt(final byte[] buffer, final int offset){
        return    (buffer[offset+3]) << 24
                | (buffer[offset+2] & 0xff) << 16
                | (buffer[offset+1] & 0xff) << 8
                | (buffer[offset+0] & 0xff);
    }

    public static long readUnsignedInt(final byte[] buffer, final int offset){
        return    (long) (buffer[offset+3] & 0xff) << 24
                | (long) (buffer[offset+2] & 0xff) << 16
                | (long) (buffer[offset+1] & 0xff) << 8
                | (long) (buffer[offset+0] & 0xff);
    }

    public static long readLong(final byte[] buffer, final int offset){
        return    (long) (buffer[offset+7]) << 56
                | (long) (buffer[offset+6] & 0xff) << 48
                | (long) (buffer[offset+5] & 0xff) << 40
                | (long) (buffer[offset+4] & 0xff) << 32
                | (long) (buffer[offset+3] & 0xff) << 24
                | (long) (buffer[offset+2] & 0xff) << 16
                | (long) (buffer[offset+1] & 0xff) << 8
                | (long) (buffer[offset+0] & 0xff);
    }

    public static float readFloat(final byte[] buffer, final int offset){
        return Float.intBitsToFloat(readInt(buffer,offset));
    }

    public static double readDouble(final byte[] buffer, final int offset){
        return Double.longBitsToDouble(readLong(buffer,offset));
    }

}
