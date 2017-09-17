package MIDI;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class ReadMidi {

	/**
	 * ��ȡmidi��Ϣ
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
		// ���û�ȡMTHD��Ϣ�����õ�ͷ����Ϣ
		HeaderChunks headerChunks = this.readHeaderChunks(byteBuffer);
		List<TrackChunk> trackChunklist = this.readTreackChunks(byteBuffer,
				headerChunks);
		this.outInforMidi(headerChunks, trackChunklist);
	}

	public void outInforMidi(HeaderChunks headerChunks,
			List<TrackChunk> trackChunklist) {
		System.out.println("-------------MThd-----------------");
		System.out.println("ͷ������:" + new String(headerChunks.getMidiId()));
		System.out.println("ͷ�鳤��:" + headerChunks.getLength());
		System.out.println("��ʽ:" + headerChunks.getFormat());
		System.out.println("������:" + headerChunks.getTrackNumber());
		System.out.println("����:" + headerChunks.getMidiTimeSet());
		for (int i = 0; i < trackChunklist.size(); i++) {
			System.out.println("-------------MTrk-----------------");
			System.out.println("�����" + i + ":"
					+ new String(trackChunklist.get(i).getMidiId()));
			System.out.println("�������ݳ���:" + trackChunklist.get(i).getLength());
			//trackChunklist.get(i).getData();
			trackChunklist.get(i).transfer();
		}
		//trackChunklist.get(0).printResult();
	}

	/**
	 * ��ȡͷ����ϢMThd
	 * 
	 * @param buffer
	 * @return
	 */
	public HeaderChunks readHeaderChunks(ByteBuffer buffer) {
		headerChunks = new HeaderChunks();
		for (int i = 0; i < headerChunks.getMidiId().length; i++) {
			headerChunks.getMidiId()[i] = (byte) (buffer.get());	//ѭ����ȡLength���ֽ�
		}
		headerChunks.setLength(buffer.getInt());	//������4���ֽ�
		headerChunks.setFormat(buffer.getShort());
		headerChunks.setTrackNumber(buffer.getShort());	//�����������ֽ�
		headerChunks.setMidiTimeSet(buffer.getShort());
		return headerChunks;
	}

	/**
	 * ��ȡMTrk��Ϣ��
	 * 
	 * @param buffer
	 * @param headerChunks
	 * @return
	 */
	public List<TrackChunk> readTreackChunks(ByteBuffer buffer,
			HeaderChunks headerChunks) {
		trackChunklist = new ArrayList<TrackChunk>();
		
		TrackChunk trackChunk;
		for (int i = 0; i < headerChunks.getTrackNumber(); i++) {	//�������е�����
			trackChunk = new TrackChunk();
			for (int j = 0; j < trackChunk.getMidiId().length; j++) {
				trackChunk.getMidiId()[j] = (byte) (buffer.get());	//��ȡ"MTrk"
			}
			trackChunk.setLength(buffer.getInt());	//�ٶ�4�ֽڣ���Ϊ���쳤��
			byte[] data = new byte[trackChunk.getLength()];	//����length���ֽڵ��ڴ棬�����洢����
			buffer.get(data);	//���ֽڵĶ�ȡʣ�������
			//System.out.println(data[0]);
			trackChunk.setData(data);
			
			trackChunklist.add(trackChunk);	//������i��������ӵ�����������
			
		}
		
		return trackChunklist;
	}

}