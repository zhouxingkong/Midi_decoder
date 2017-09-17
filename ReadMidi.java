package MIDI;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class ReadMidi {

	/**
	 * 获取midi信息
	 * 
	 * @param midiFile
	 * @throws Exception
	 */
	private ArrayList<TrackChunk> trackChunklist;
	private HeaderChunks headerChunks;
	
	public ArrayList<TrackChunk> getTrackChunkList(){
		return trackChunklist;
	}
	public HeaderChunks get_headerChunks(){
		
		return headerChunks;
	}

	public void readDateFIle(File midiFile) throws Exception {
		FileInputStream stream = new FileInputStream(midiFile);
		byte[] data = new byte[stream.available()];
		stream.read(data);
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		// 调用获取MTHD信息方法得到头部信息
		HeaderChunks headerChunks = this.readHeaderChunks(byteBuffer);
		List<TrackChunk> trackChunklist = this.readTreackChunks(byteBuffer,
				headerChunks);
		this.outInforMidi(headerChunks, trackChunklist);
	}

	public void outInforMidi(HeaderChunks headerChunks,
			List<TrackChunk> trackChunklist) {
		System.out.println("-------------MThd-----------------");
		System.out.println("头块类型:" + new String(headerChunks.getMidiId()));
		System.out.println("头块长度:" + headerChunks.getLength());
		System.out.println("格式:" + headerChunks.getFormat());
		System.out.println("音轨数:" + headerChunks.getTrackNumber());
		System.out.println("分区:" + headerChunks.getMidiTimeSet());
		for (int i = 0; i < trackChunklist.size(); i++) {
			System.out.println("-------------MTrk-----------------");
			System.out.println("音轨块" + i + ":"
					+ new String(trackChunklist.get(i).getMidiId()));
			System.out.println("音轨数据长度:" + trackChunklist.get(i).getLength());
			//trackChunklist.get(i).getData();
			trackChunklist.get(i).transfer();
		}
		//trackChunklist.get(0).printResult();
	}

	/**
	 * 获取头部信息MThd
	 * 
	 * @param buffer
	 * @return
	 */
	public HeaderChunks readHeaderChunks(ByteBuffer buffer) {
		headerChunks = new HeaderChunks();
		for (int i = 0; i < headerChunks.getMidiId().length; i++) {
			headerChunks.getMidiId()[i] = (byte) (buffer.get());	//循环读取Length个字节
		}
		headerChunks.setLength(buffer.getInt());	//连续读4个字节
		headerChunks.setFormat(buffer.getShort());
		headerChunks.setTrackNumber(buffer.getShort());	//连续读两个字节
		headerChunks.setMidiTimeSet(buffer.getShort());
		return headerChunks;
	}

	/**
	 * 获取MTrk信息块
	 * 
	 * @param buffer
	 * @param headerChunks
	 * @return
	 */
	public List<TrackChunk> readTreackChunks(ByteBuffer buffer,
			HeaderChunks headerChunks) {
		trackChunklist = new ArrayList<TrackChunk>();
		
		TrackChunk trackChunk;
		for (int i = 0; i < headerChunks.getTrackNumber(); i++) {	//遍历所有的音轨
			trackChunk = new TrackChunk();
			for (int j = 0; j < trackChunk.getMidiId().length; j++) {
				trackChunk.getMidiId()[j] = (byte) (buffer.get());	//读取"MTrk"
			}
			trackChunk.setLength(buffer.getInt());	//再度4字节，作为音轨长度
			byte[] data = new byte[trackChunk.getLength()];	//分配length个字节的内存，用来存储数据
			buffer.get(data);	//逐字节的读取剩余的数据
			//System.out.println(data[0]);
			trackChunk.setData(data);
			
			trackChunklist.add(trackChunk);	//将音轨i的数据添加到音轨链表中
			
		}
		
		return trackChunklist;
	}

}