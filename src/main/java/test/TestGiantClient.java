package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.giant.htmlunit.fetch.FetchWebContext;
import com.giant.htmlunit.vo.fetch.ReloadContextVo;


/**
 * giant-htmlunit可以优秀的处理webclient多线程抓取网页，以便htmlunit更好的支持代理ip，拨号ip。节省更多的硬件资源。
 * 本用例用于测试 giant-htmlunit，处理js文件进行测试，（支持gzip流格式文件的修改）
 * @author lyq
 */
public class TestGiantClient {

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {

		updateJs();
//		setWait();
//		replaceJsResoure();//如果修改原始js太麻烦了,可以把js下载回来修改，修改完替换整个js
//		skipUrl();
	}
	
	
	

	/**
	 * @throws IOException 
	 * cacheTest1.html 引入js1.js文件，修改js1.js代码，并执行。
	 * 
	 */
	public static void updateJs() throws IOException{
		
		FetchWebContext context = new FetchWebContext();
		ReloadContextVo contextVo = new ReloadContextVo();
		
		Map<String, ReloadContextVo> map = new HashedMap();
		map.put("/js/js", contextVo);//识别url，如果url改变了，这个标识不变的情况下会重新加载js
		context.getFetchWebClient().cacheFile("/js/js");
		contextVo.setStatic(true);//如果只替换一次，那么设置成true；如果需要动态替换，设置成false
//		contextVo.add(arg0, arg1);
		contextVo.add("(func1\\(\\)[\\w\\W]+?return\\s+?\")[\\w\\W]+?(\")","$1方法func1被修改了。。。$2");//类似 string。replaceAll方法，替换js代码
		context.getFetchWebClient().setRegexToValue(map);
		try{
			HtmlPage page = (HtmlPage)context.getFetchWebClient().getPage("http://127.0.0.1:8085/test1/cacheTest1.html");
			//			context.getFetchWebClient().set
			 Object result = context.getFetchWebClient().getJavaScriptEngine().execute(page,"func1()" , "injected script", 1);
			System.out.println(result);
		}catch(Exception e){
			context.asynCloseWebAllWindows();
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 * testWait.html 引入js文件waitJs.js文件，主程序等待waitJs。js里面的代码执行完毕再继续跑。
	 * addFilter× 与 waitFilter× 配对方法； 等待浏览器执行完js指定输出后再继续跑主程序
	 */
	public static void setWait() throws IOException{
		FetchWebContext context = new FetchWebContext();
		try{
			context.getFetchWebClient().addFilterConsoleForStop("wait log1");
			HtmlPage html = (HtmlPage)context.getFetchWebClient().getPage("http://127.0.0.1:8085/test1/testWait.html");
//			context.getFetchWebClient().set
			Long start = System.currentTimeMillis();
			//这里找不到waitJs.js输出日志  "wait log1",所以会等待十秒钟，执行失败
 			context.getFetchWebClient().waitFilterConsoleForStop("wait log1", 10000);
//			String result = context.getFetchWebClient().getConsoleResult();
 			
			System.out.println(context.getFetchWebClient().getFilterUrlMap().size()+"花费时间："+(System.currentTimeMillis()-start));
			
			
			context.getFetchWebClient().addFilterConsoleForStop("wait log");
			 html = (HtmlPage)context.getFetchWebClient().getPage("http://127.0.0.1:8085/test1/testWait.html");
//			context.getFetchWebClient().set
			 start = System.currentTimeMillis();
			//这里找不到waitJs.js输出日志  "wait log1",所以会等待js输出 wait log，执行成功
 			context.getFetchWebClient().waitFilterConsoleForStop("wait log", 10000);
//			String result = context.getFetchWebClient().getConsoleResult();
 			
			System.out.println(context.getFetchWebClient().getFilterUrlMap().size()+"花费时间："+(System.currentTimeMillis()-start));
			
		}catch(Exception e){
			context.asynCloseWebAllWindows();
		}
	}
	
	
	/**
	 * @throws IOException 
	 *testWait.html 引入js文件js1.js文件；主程序获取js2.js代替js1.js内容；
	 * 
	 */
	public static void replaceJsResoure() throws IOException{
		FetchWebContext context = new FetchWebContext();
		ReloadContextVo vo = new ReloadContextVo();
		context.getFetchWebClient().cacheFile("js/js");
		try{
			
			context.url2url("http://127.0.0.1:8085/js/js1.js", "http://127.0.0.1:8085/js/js2.js");
			context.getFetchWebClient().addFilterConsoleForResult("my result:");
			HtmlPage html = (HtmlPage)context.getFetchWebClient().getPage("http://127.0.0.1:8085/test1/cacheTest1.html");
//			context.getFetchWebClient().set
 			context.getFetchWebClient().waitFilterConsoleResult("my result:", 1000);
			String result = context.getFetchWebClient().getConsoleResult();
			System.out.println(result);
		}catch(Exception e){
			context.asynCloseWebAllWindows();
		}
	}
	
	/**
	 * @throws IOException 
	 * 跳过指定路径不获取
	 * 
	 */
	public static void skipUrl() throws IOException{
		FetchWebContext context = new FetchWebContext();
		ReloadContextVo vo = new ReloadContextVo();
		try{
			
			context.getSkipUrls().add("/js/js");
			HtmlPage html = (HtmlPage)context.getFetchWebClient().getPage("http://127.0.0.1:8085/test1/cacheTest1.html");
			/***
			 * 这里跳过了含/js/js了路径
			 */
			System.out.println(html);
		}catch(Exception e){
			e.printStackTrace();
			context.asynCloseWebAllWindows();
		}
	}
}
