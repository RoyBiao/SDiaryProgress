package com.loovee.common.xmpp.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.loovee.common.app.LooveeApplication;
import com.loovee.common.bean.BaseIQResults;
import com.loovee.common.bean.BaseReqIQParams;
import com.loovee.common.util.ALFileManager;
import com.loovee.common.util.LogUtils;
import com.loovee.common.util.NetUtil;
import com.loovee.common.xmpp.bean.Mediaserver;
import com.loovee.common.xmpp.core.Chat;
import com.loovee.common.xmpp.core.PacketListener;
import com.loovee.common.xmpp.core.User;
import com.loovee.common.xmpp.core.UserAuthentication;
import com.loovee.common.xmpp.core.XMPPConnection;
import com.loovee.common.xmpp.core.XMPPException;
import com.loovee.common.xmpp.exception.NoNetworkException;
import com.loovee.common.xmpp.filter.IQQueryXmlnsFilter;
import com.loovee.common.xmpp.packet.DefaultIQ;
import com.loovee.common.xmpp.packet.IQ;
import com.loovee.common.xmpp.packet.Message;
import com.loovee.common.xmpp.packet.Packet;
import com.loovee.common.xmpp.packet.ParamsIQ;
import com.loovee.common.xmpp.packet.XMPPError;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class XMPPUtils {
	private static Handler mHandler = new Handler(Looper.getMainLooper());
	private static int SEND_IQ_TIMEOUT = 40 * 1000;//40秒
	private static ExecutorService sendMessageThreadPool = Executors.newSingleThreadExecutor();

	/**
	 * 获取一个登陆的XMPPConnection，不能保证一定是登陆，只能保证如果能登陆，则先登陆再返回。
	 * 
	 * @return
	 * @throws NoNetworkException
	 * @throws XMPPException
	 */
	public static XMPPConnection getLoginConnection() {
		XMPPConnection connection = LooveeApplication.instances
				.getXMPPConnection();
		try {
			if (!connection.isConnected()) {
				connection.connect();
			}
			if (!connection.isAuthenticated()) {
				connection.login();
			}
			LogUtils.jLog().e("获取了一个xmppConnection");
			return connection;
		} catch (XMPPException e) {
			LogUtils.jLog().e("XMPPException = "+e.getMessage());
			e.printStackTrace();
		}

		return connection;
	}
	
	
	/**
	 * 返回一个已经连接的xmppConnection,不能保证一定是已经连接的
	 * @return
	 */
	public static XMPPConnection getConnection() {
		XMPPConnection connection = LooveeApplication.instances.getXMPPConnection();
		try {
			if (!connection.isConnected()) {
				connection.connect();
			}
			return connection;
		} catch (XMPPException e) {
			LogUtils.jLog().e("XMPPException = "+e.getMessage());
			e.printStackTrace();
		}
		return connection;
	}
		
	

	/**
	 * 返回上传的地址
	 * 
	 * @return
	 * @throws NoNetworkException
	 * @throws XMPPException
	 */
	public static String getUploadUrl() {
		Mediaserver serMediaserver = XMPPConnection.getDispatcher().getMediaserver();
		StringBuffer buffer = new StringBuffer("http://");
		buffer.append(serMediaserver.getIp());
		buffer.append(":");
		buffer.append(serMediaserver.getPort());
		final String userName = StringUtils.parseName(XMPPConnection.getUser().getJid());
		buffer.append("/MediaServer/photo?filename=photo.jpg&username="
				+ userName + "&type=upload&needreturndata=false");
		return buffer.toString();
	}

	/**
	 * 返回上传地址
	 * 
	 * @return
	 * @throws NoNetworkException
	 * @throws XMPPException
	 */
	public static String getUploadAudioUrl(String filepath) {
		Mediaserver serMediaserver = XMPPConnection.getDispatcher()
				.getMediaserver();
		StringBuffer buffer = new StringBuffer("http://");
		buffer.append(serMediaserver.getIp());
		buffer.append(":");
		buffer.append(serMediaserver.getPort());
		String fileName = "";
		try {
			fileName = URLEncoder.encode(ALFileManager.getFileName(filepath),
					"UTF-8");
		} catch (UnsupportedEncodingException e) {
			fileName = "";
		}
		final String userName = StringUtils.parseName(XMPPConnection.getUser().getJid());
		buffer.append("/MediaServer/audio?filename=" + fileName + "&username="
				+ userName + "&type=upload&needreturndata=false");
		return buffer.toString();
	}

	/**
	 * 返回下载的url
	 * 
	 * @return
	 */
	public static String getdownloadUrl(String id) {
		if (TextUtils.isEmpty(id)) {
			return "";
		}
		if (XMPPConnection.getDispatcher() == null) {
			return "";
		}
		Mediaserver serMediaserver = XMPPConnection.getDispatcher()
				.getMediaserver();

		StringBuffer buffer = new StringBuffer("http://");
		buffer.append(serMediaserver.getIp());
		buffer.append(":");
		buffer.append(serMediaserver.getPort());
		buffer.append("/MediaServer/photo?fileid=" + id);
		return buffer.toString();
	}

	/**
	 * 拼接语音媒体下载地址
	 * 
	 * @param audioFileId
	 * @return
	 * @throws NoNetworkException
	 * @throws XMPPException
	 */
	public static String getDownLoadAudioUrl(String audioFileId) {
		if (TextUtils.isEmpty(audioFileId)) {
			return "";
		}
		Mediaserver serMediaserver = XMPPConnection.getDispatcher()
				.getMediaserver();
		StringBuffer buffer = new StringBuffer("http://");
		buffer.append(serMediaserver.getIp());
		buffer.append(":");
		buffer.append(serMediaserver.getPort());
		buffer.append("/MediaServer/audio?fileid=" + audioFileId);
		return buffer.toString();
	}

	/**
	 * 发送一个IQ给服务器
	 * 
	 * @param params
	 *            IQ的参数
	 * @param listener
	 *            数据回调
	 * @param to
	 * @throws NoNetworkException 
	 */
	public static <T extends BaseReqIQParams, R extends BaseIQResults> void sendIQ(
			T params, final OnIQRespondListener<R> listener, String to)
			throws NoNetworkException {
		sendIQ(params, listener, to, IQ.Type.GET);
	}


	/**
	 * 发送消息
	 * @param message
	 */
	public static void sendMessage(final Message message) {
		sendMessageThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				Chat chat = getLoginConnection().getChatManager().createChat(message.getTo(),
						null);
				try {
					chat.sendMessage(message);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * 发送一个IQ给服务器
	 * 
	 * @param params
	 *            IQ的参数
	 * @param listener
	 *            数据回调
	 * @param to
	 * @param type
	 *            IQ的类型
	 * @throws NoNetworkException
	 * @throws XMPPException
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BaseReqIQParams, R extends BaseIQResults> void sendIQ(
			final T params, final OnIQRespondListener<R> listener,
			final String to, final IQ.Type type) throws NoNetworkException {
		if (!NetUtil.isNetworkAvailable(LooveeApplication.getLocalLoovee())) {
			throw new NoNetworkException("network is not connect");
		}

		new Thread(new Runnable() {
			boolean isResponsed = false; //服务器是否已经返回，默认没有返回
			@Override
			public void run() {
				try {
					final XMPPConnection connection = getLoginConnection();
					final Timer timeoutTimer = new Timer();
					final PacketListener packetListener = new PacketListener() {

						@Override
						public void processPacket(final Packet packet) {
							isResponsed = true;
							timeoutTimer.cancel(); //取消超时定时器
							if (packet.getError() == null) {
								final Object result = ((DefaultIQ<Object>) packet).getData();
								if (listener != null) {
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											listener.onRespond(
													params.getXmlns(),
													(R) result);

										}
									});

								}
							} else {
								if (listener != null) {
									mHandler.post(new Runnable() {

										@Override
										public void run() {
											listener.OnError(params.getXmlns(),
													packet.getError());
										}
									});

								}
							}
							connection.removePacketListener(this);
						}
					};
					connection.addPacketListener(packetListener, new IQQueryXmlnsFilter(params.getXmlns()));
					ParamsIQ iq = new ParamsIQ(params);
					iq.setTo(to);
					iq.setType(type);
					connection.sendPacket(iq);
					timeoutTimer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							if(!isResponsed) { //证明已经超时
								LogUtils.jLog().d("IQ请求超时->>>>>>>>>>");
								connection.removePacketListener(packetListener);
								mHandler.post(new Runnable() {
									
									@Override
									public void run() {
										//超时
										listener.OnError(params.getXmlns(), new XMPPError(XMPPError.CODE_TIME_OUT, "timeout"));
									}
								});
								
							}
						}
					}, SEND_IQ_TIMEOUT);
				} catch (IllegalStateException e) {
					LogUtils.jLog().d("IllegalStateException");
					e.printStackTrace();
				}
			}
		}).start();

	}


	/**
	 * 登录
	 * @param account 账号
	 * @param password 密码
	 * @param type 登录类型
	 * @param listener 登录回调
	 */
	public static void login(final String account,final String password,final UserAuthentication.LoginType type, final OnXMPPLoginListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				XMPPConnection connection = getConnection();
				try {
					connection.login(account, password, type); // 登录
				} catch (final XMPPException e) {
					if(listener != null) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								listener.OnError(e);
							}
						});

					}
				}
				if (connection.isAuthenticated()) {
					final User user = connection.getUser();
					if(listener != null) {
						mHandler.post(new Runnable() {
							@Override
							public void run() {
								listener.onSuccess(user);
							}
						});

					}
				}

			}
		}).start();

	}


	/**
	 * 注销登录
	 */
	public static void logout() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				XMPPConnection.cleanLoginInfo();
				XMPPConnection connection = LooveeApplication.instances.getXMPPConnection();
				if (connection != null && connection.isConnected()) {
					connection.disconnect();
				}
			}
		}).start();

	}

	
	/**
	 *  IQ数据返回监听者
	 * @author Jesse
	 *
	 * @param <T>
	 */
	public interface OnIQRespondListener<T extends BaseIQResults> {
		/**
		 * 返回数据回调
		 * 
		 * @param xmlns
		 *            对应的命名空间
		 * @param results
		 *            返回的数据
		 */
		public void onRespond(String xmlns, T results);

		/**
		 * 错误回调
		 * 
		 * @param xmlns
		 *            对应的命名空间
		 * @param err
		 *            对应的错误
		 */
		public void OnError(String xmlns, XMPPError err);

	}


	/**
	 *  XMPP登录监听回调
	 * @author Jesse
	 *
	 */
	public interface OnXMPPLoginListener {
		/**
		 * 登录成功回调
		 * @param results 用户资料
		 */
		public void onSuccess(User results);

		/**
		 * 登录失败回调
		 * @param exception
		 */
		public void OnError(XMPPException exception);

	}


}
