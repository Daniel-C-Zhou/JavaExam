package cn.zhou;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.Objects;

@Slf4j
public class SDFParseUtil {

	public static JSONArray file2JSONData(String fileName) {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		JSONArray array = new JSONArray();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));//构造一个BufferedReader类来读取文件
			String start = "untitled.mol";//一个对象的开始标识
			String end = "$$$$";//一个对象的结束标识
			String line;//按行读取的数据
			JSONObject object = null;//一条数据
			StringBuilder data = new StringBuilder();
			String prop = "";//属性
			String value = "";//属性值
			while ((line = br.readLine()) != null) {//使用readLine方法，一次读一行
				data.append(System.lineSeparator() + line);
				if (Objects.equals(end, line)) {//表示一个对象读取完毕，然后将对象放入返回集合中
					array.add(object);
				}
				if (Objects.equals(start, line)) {//表示一个对象开始
					object = new JSONObject();
				} else if (Objects.equals(line, "M  END")) {//Structure属性读取结束
					object.put("Structure", data.toString());
				} else if (line.contains("> <")) {//属性读取开始
					prop = line.substring(3, line.length() - 1);//从行数据中取出属性
				} else if (StringUtils.isNotBlank(line)) {//读取属性值
					value = line;
				} else if (StringUtils.isNotBlank(prop) && StringUtils.isBlank(line)) {//当读取到一个空行且属性不为空时表示当前读取属性和属性值完毕
					object.put(prop, value);
				}
			}
			br.close();
		} catch (Exception e) {
			log.error("read data error");
			e.printStackTrace();
		}
		return array;
	}

	public static void jsonData2File(String res, URL filePath) {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		try {
			File distFile = new File(filePath.toURI());
			bufferedReader = new BufferedReader(new StringReader(res));
			bufferedWriter = new BufferedWriter(new FileWriter(distFile));
			char buf[] = new char[1024];//字符缓冲区
			int len;
			while ((len = bufferedReader.read(buf)) != -1) {
				bufferedWriter.write(buf, 0, len);
			}
			bufferedWriter.flush();
			bufferedReader.close();
			bufferedWriter.close();
		} catch (Exception e) {
			log.error("write data error");
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					log.error("close buffer error");
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		String inputFileName = "sample_1.sdf";
		JSONArray array = file2JSONData(inputFileName);
		String outputFileName = "sample_1.json";
		URL url = Thread.currentThread().getContextClassLoader().getResource(outputFileName);
		String jsonData = JSON.toJSONString(array, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat);
		jsonData2File(jsonData, url);
	}
}
