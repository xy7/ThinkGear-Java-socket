package com.seasun.neurosky;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import processing.core.PApplet;
import processing.core.PFont;

public class DataCollectApp extends PApplet implements EventHandle{

	private static final long serialVersionUID = 1L;
	public ThinkGearSocket neuroSocket;
	public int attention = 10;
	public int meditation = 10;
	public PFont font;
	
	public NamedParameterJdbcTemplate jdbc;
	
	public void initJdbc(){
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(Utils.getOrDefault("datasource.url", "jdbc:mysql://localhost:3306/mindware?useUnicode=true&characterEncoding=UTF-8") );
		ds.setUsername(Utils.getOrDefault("datasource.username", "root") );
		ds.setPassword(Utils.getOrDefault("datasource.password", "123456") );
		ds.setDriverClassName(Utils.getOrDefault("datasource.driver-class-name", "com.mysql.jdbc.Driver") );
		jdbc = new NamedParameterJdbcTemplate(ds);
	}

	public void setup() {
		size(600, 600);
		
		initJdbc();
		
		/*  //目前有问题
		String appName = "seasun_raw_data_collect_1";
		String appKey = util.Utils.SHA1(appName);
		ThinkGearSocket neuroSocket = new ThinkGearSocket(this, appName, appKey);
		*/
		ThinkGearSocket neuroSocket = new ThinkGearSocket(this);
		try {
			neuroSocket.start();
		} catch (Exception e) {
			println(e.toString());
			println("Is ThinkGear running??");
		}
		smooth();
		// noFill();
		font = createFont("Verdana", 12);
		textFont(font);
	}

	public void draw() {
		// background(0,0,0,50);
		fill(0, 0, 0, 255);
		noStroke();
		rect(0, 0, 120, 80);

		fill(0, 0, 0, 10);
		noStroke();
		rect(0, 0, width, height);
		fill(0, 116, 168);
		stroke(0, 116, 168);
		text("Attention: " + attention, 10, 30);
		noFill();
		ellipse(width / 2, height / 2, attention * 3, attention * 3);

		fill(209, 24, 117, 100);
		noFill();
		text("Meditation: " + meditation, 10, 50);
		stroke(209, 24, 117, 100);
		noFill();
		ellipse(width / 2, height / 2, meditation * 3, meditation * 3);
	}

	@Override
	public void poorSignalEvent(LocalDateTime time, int sig) {
		println(time + " SignalEvent " + sig);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("sig", sig);
		paramMap.put("time", time.toString().replace("T", " "));
		jdbc.update("insert into poor_signal(signal_level, receive_time) values(:sig, :time)", paramMap);
	}
	
	@Override
	public void esenseEvent(LocalDateTime time, int attentionLevel, int meditationLevel){
		this.attention = attentionLevel;
		this.meditation = meditationLevel;
		println(time + " attentionLevel: " + attentionLevel + " meditationLevel: " + meditationLevel);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("attention", attention);
		paramMap.put("meditation", meditation);
		paramMap.put("time", time.toString().replace("T", " "));
		jdbc.update("insert into esense(attention, meditation, receive_time) values(:attention, :attention, :time)", paramMap);
	}
	

	@Override
	public void blinkEvent(LocalDateTime time, int blinkStrength) {
		println(time + " blinkStrength: " + blinkStrength);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("blinkStrength", blinkStrength);
		paramMap.put("time", time.toString().replace("T", " "));
		jdbc.update("insert into blink(blink_strength, receive_time) values(:blinkStrength, :time)", paramMap);
	}

	@Override
	public void eegPowerEvent(LocalDateTime time
			, int delta, int theta, int low_alpha, int high_alpha
			, int low_beta, int high_beta,int low_gamma, int mid_gamma) {
		println(time + " eegPower:");
		println("delta Level: " + delta);
		println("theta Level: " + theta);
		println("low_alpha Level: " + low_alpha);
		println("high_alpha Level: " + high_alpha);
		println("low_beta Level: " + low_beta);
		println("high_beta Level: " + high_beta);
		println("low_gamma Level: " + low_gamma);
		println("mid_gamma Level: " + mid_gamma);
		
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("delta", delta);
		paramMap.put("theta", theta);
		paramMap.put("low_alpha", low_alpha);
		paramMap.put("high_alpha", high_alpha);
		paramMap.put("low_beta", low_beta);
		paramMap.put("high_beta", high_beta);
		paramMap.put("low_gamma", low_gamma);
		paramMap.put("mid_gamma", mid_gamma);
		paramMap.put("time", time.toString().replace("T", " "));
		jdbc.update("insert into eeg_power(delta, theta, low_alpha, high_alpha, low_beta"
				+ ", high_beta, low_gamma, mid_gamma"
				+ " , receive_time) values(:delta, :theta, :low_alpha, :high_alpha, :low_beta"
				+ ", :high_beta, :low_gamma, :mid_gamma"
				+ ", :time)", paramMap);
	}

	@Override
	public void rawEegEvent(LocalDateTime time, int raw, int index) {
		println(time + " rawEvent Level: " + raw + " index: " + index);
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("raw", raw);
		paramMap.put("index", index);
		paramMap.put("time", time.toString().replace("T", " "));
		jdbc.update("insert into raw_eeg(raw_eeg, index_, receive_time) values(:raw, :index, :time)", paramMap);
	}
	
	@Override
	public void delay(){
		this.delay(50);
	}

	public void stop() {
		neuroSocket.stop();
		super.stop();
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { com.seasun.neurosky.DataCollectApp.class.getName() });
	}
}
