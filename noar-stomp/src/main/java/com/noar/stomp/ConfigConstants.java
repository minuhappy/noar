package com.noar.stomp;

public class ConfigConstants {
	public static final String STOMP_END_POINT_PATH = "/elidom/stomp";
	public static final String STOMP_DESTINATION_PREFIXES = "stomp.destination.prefixes";
	
	public static final String STOMP_BUFFER_LIMIT_SIZE = "stomp.buffer.size.limit";
	public static final String STOMP_SEND_MESSAGE_LIMIT_SIZE = "stomp.send.message.size.limit";
	public static final String STOMP_SEND_TIME_LIMIT_MS = "stomp.send.time.limit.ms";
	
	/**
	 * WebSocket 최대 텍스트 메시지 버퍼 사이즈 설정 키 : websocket.max.text.message.buffer.size 
	 */
	public static final String WEBSOCKET_MAX_TEXT_MESSAGE_BUFFER_SIZE = "websocket.max.text.message.buffer.size";

	/**
	 * WebSocket 최대 바이너리 버퍼 사이즈 설정 키 : websocket.max.binary.message.buffer.size
	 */
	public static final String WEBSOCKET_MAX_BINARY_MESSAGE_BUFFER_SIZE = "websocket.max.binary.message.buffer.size";
	
	public static final String WEBSOCKET_MAX_SESSION_IDLE_TIMEOUT = "websocket.max.session.idle.timeout";
	public static final String WEBSOCKET_ASYNC_SEND_TIMEOUT = "websocket.async.send.timeout";
}
