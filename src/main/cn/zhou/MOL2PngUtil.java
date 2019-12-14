package cn.zhou;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.*;

@Slf4j
public class MOL2PngUtil {

	public static String doPost(String url, JSONObject json) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			StringEntity s = new StringEntity(json.toString());
			s.setContentEncoding("UTF-8");
			s.setContentType("application/json");//发送json数据需要设置contentType
			post.setEntity(s);
			HttpResponse res = client.execute(post);
			if (res.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return EntityUtils.toString(res.getEntity());// 返回json格式：
			}
		} catch (Exception e) {
			log.error("get png data error");
			e.printStackTrace();
		}
		return null;
	}

	private static void getWritePng(String url, JSONObject object) {
		JSONObject data = object;
		String chemData = data.getString("Structure");
		String ID = data.getString("ID");
		JSONObject param = new JSONObject();
		param.put("chemData", chemData);
		param.put("chemDataType", "chemical/x-mdl-molfile");
		param.put("imageType", "image/png");
		param.put("width", "400");
		param.put("height", "200");
		String pngData = doPost(url, param);
		BufferedOutputStream outStream = null;
		BufferedReader inStream = null;
		try {
			outStream = new BufferedOutputStream(new FileOutputStream(ID + ".png"));
			inStream = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pngData.getBytes())));
			outStream.write(pngData.getBytes());
		} catch (Exception e) {
			log.error("write png error");
			e.printStackTrace();
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				log.error("close stream error");
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String url = "https://chemdrawdirect.perkinelmer.cloud/rest/generateImage";
		String inputFileName = "sample_1.sdf";
		JSONArray array = SDFParseUtil.file2JSONData(inputFileName);
		for (Object object : array) {
			//多线程异步请求写入数据
			ThreadConstant.fixedThreadPool.submit(() -> {
				try {
					getWritePng(url, (JSONObject) object);
				} catch (Exception e) {
					log.error("get and write png error");
					e.printStackTrace();
				}
			});
		}
	}


}
