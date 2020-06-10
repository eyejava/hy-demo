package com.hmm.jade.demo;



import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class ShopTest {

    public static CloseableHttpClient httpclient = HttpClients.createDefault();


    public static void getFirtPageData(String keyword) throws IOException {

        String input = "奶粉";
// 需要爬取商品信息的网站地址
        String url = "https://list.tmall.com/search_product.htm?q=" + keyword+"";
   //     url ="https://list.tmall.com/search_product.htm?spm=a220m.1000858.0.0.4afb37f3CkQi5s&s=360&q=%C4%CC%B7%DB&sort=s&style=g&type=pc#J_Filter";
// 动态模拟请求数据
        System.out.println("=========="+ keyword +"-------------------------------");
        httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
// 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
// 获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
        try {
            HttpEntity entity = response.getEntity();
            // 如果状态响应码为200，则获取html实体内容或者json文件
            if(statusCode == 200){
                String html = null;
                try {
                    html = EntityUtils.toString(entity, Consts.UTF_8);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 提取HTML得到商品信息结果
                Document doc = null;
                // doc获取整个页面的所有数据
                doc = Jsoup.parse(html);
                //输出doc可以看到所获取到的页面源代码
//      System.out.println(doc);
                // 通过浏览器查看商品页面的源代码，找到信息所在的div标签，再对其进行一步一步地解析
                Elements ulList = doc.select("div[class='view grid-nosku']");

                if(ulList.size() == 0){
                    ulList = doc.select("div[class='view ']");
                }
                Elements liList = ulList.select("div[class='product']");
                // 循环liList的数据（具体获取的数据值还得看doc的页面源代码来获取，可能稍有变动）
                for (Element item : liList) {
                    // 商品ID
                    String id = item.select("div[class='product']").select("p[class='productStatus']").select("span[class='ww-light ww-small m_wangwang J_WangWang']").attr("data-item");
                    System.out.println("商品ID："+id);
                    // 商品名称
                    String name = item.select("p[class='productTitle']").select("a").attr("title");
                    System.out.println("商品名称："+name);
                    // 商品价格
                    String price = item.select("p[class='productPrice']").select("em").attr("title");
                    System.out.println("商品价格："+price);
                    // 商品网址
                    String goodsUrl = item.select("p[class='productTitle']").select("a").attr("href");
                    System.out.println("商品网址："+goodsUrl);

                    collectGoods(goodsUrl);

                    // 商品图片网址
                    String imgUrl = item.select("div[class='productImg-wrap']").select("a").select("img").attr("data-ks-lazyload");
                    System.out.println("商品图片网址："+imgUrl);
                    System.out.println("------------------------------------");
                }
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            } else {
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
       //     response.close();
        }
    }

    private static void collectGoods(String goodsUrl) throws IOException, InterruptedException {
        HttpEntity entity;
        int statusCode;
        String html;
        HttpPost post = new HttpPost("http://sapi.hanmama.com/vendor/index.php?app=product_collection&mod=start_collection&inajax=1");
        post.addHeader(new BasicHeader("Cookie","seller_token=7be1eb82f83ddbdcf49a078b47af26aa; now_location_op=index; now_addons=; now_location_nav=indexproduct_collection; now_location_app=product_collection; PHPSESSID=921j3o35kk0ibj9ucu3eptqrp0; 0D59_msgnewnum36=1; 0D59_vendor_key=3MQbyLNYg4tayK-nfIMHjJwI0K9kLGgDsVLy5Wcjqe5DbFcVQNVJMG6YsSwILESE7E1Ztkb5cGyJby5Wcjc5bFwXQML5xK5IcSgG8DL5ww6"));
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        //建立一个NameValuePair数组，用于存储欲传送的参数
        params.add(new BasicNameValuePair("source_url_data","http:"+goodsUrl));

        //添加参数
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        CloseableHttpResponse goodResponse = httpclient.execute(post);
        entity = goodResponse.getEntity();
        statusCode = goodResponse.getStatusLine().getStatusCode();

        if(statusCode == 200) {
            html = null;
            try {
                html = EntityUtils.toString(entity, Consts.UTF_8);
                System.out.println(html);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Thread.sleep(60000);
    }

    public static boolean nextPage(){

        System.getProperties().setProperty("webdriver.chrome.driver", "F:/soft/chromedriver_win32/chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();

        try{
            String input = "奶粉";
// 需要爬取商品信息的网站地址
            String url = "https://list.tmall.com/search_product.htm?q=" + input;

            webDriver.get(url);
       /*     webDriver.get("http://list.taobao.com/itemlist/default.htm?cat=50000671&viewIndex=1&as=0&atype=b&style=grid&same_info=1&isnew=2&tid=0&_input_charset=utf-8");*/
            try{
                Thread.sleep(5000); // 等待浏览器加载
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            webDriver.manage().window().maximize(); // 使浏览器窗口最大化

            //js代码
            String js1 = "return document.documentElement.scrollHeight;";  // 滑动条的位置值
            String js2 = "scroll(0,10000);";

            ((JavascriptExecutor)webDriver).executeScript(js2);//执行js代码，返回浏览器滚动高度
            try{
                Thread.sleep(3000); //等待浏览器加载
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((JavascriptExecutor)webDriver).executeScript(js1);//执行js代码，滚动10000像素


            try{
                Thread.sleep(3000); // wait for web loading
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            WebElement Whref = webDriver.findElement(By.cssSelector("a.J_Ajax.btn.next"));//得到要点击的“下一页”链接

            Whref.click();//进行点击
            //webDriver.close();这句先注视掉，以便观察结果
            return true;
        }catch(org.openqa.selenium.NoSuchElementException ex){
            System.out.println("找不到要的下一页链接！");
            //webDriver.close();这句先注视掉，以便观察结果
            return false;
        }

    }

    public static void page(){
        /*    boolean result = nextPage();
        System.out.println(result);*/

        //加载驱动器
        System.setProperty("webdriver.chrome.driver","C:/soft/chromedriver_win32/chromedriver.exe");
        //打开浏览器
        WebDriver driver = new ChromeDriver();
        //打开网站
        driver.get("https://list.tmall.com/search_product.htm?q=%E5%A5%B6%E7%B2%89");
        //选择模块，linktext就是链接文本，我们可以通过linktext来查找元素
        WebElement searchBox = driver.findElement(By.linkText("下一页>>"));
        //点击该模块
        searchBox.click();
        //选择下一页模块，通过css选择器来定位元素，查找下一页按钮
        //    WebElement searchNext = driver.findElement(By.cssSelector("a[class='btn btn-xs btn-default btn-next']"));
        //查找下一页的第二种写法
        //WebElement searchNext = driver.findElement(By.cssSelector(".btn.btn-xs.btn-default.btn-next"));
        //查看该元素是否被选中，如果没有被选中，则点击选中
       /* if(!searchNext.isSelected()){
            //翻一页
            searchNext.click();
        }*/
        //等待5s
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //去当前url
        String current_url = driver.getCurrentUrl();
        System.out.println(current_url);
        //关闭窗口
        driver.close();

    }

    public static void main(String[] args) throws IOException {
        System.out.println("------------enter demo app -----------");

        String[] keys ={

                "婴幼儿宝宝护肤",
                "婴幼儿洗发/沐浴",
                "婴幼儿水杯/水壶",
                "儿童餐具"


        };

        for (int i=0;i<keys.length;i++){

            String keyword = keys[i];
            if(i != 0){
                getFirtPageData(keyword);
            }

            int base = 60;
            for (int pageNumber=0;pageNumber<10;pageNumber++){
                try {
                    getDatas(base + pageNumber * 60,keyword);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    private static void getDatas(int pageValue,String keyword) throws IOException {

     //   String input = "高景观车";
// 需要爬取商品信息的网站地址
    //    String url = "https://list.tmall.com/search_product.htm?spm=a220m.1000858.0.0.afc637f3ErIp44sort=s&style=g&from=.list.pc_1_searchbutton&type=pc#J_Filter"+"&q="+input+"&s=60";
      String  url ="https://list.tmall.com/search_product.htm?spm=a220m.1000858.0.0.afc637f3ErIp44&s="+pageValue+"&q="+keyword+"&sort=s&style=g&from=.list.pc_1_searchbutton&type=pc#J_Filter";
// 动态模拟请求数据
        System.out.println("=========="+ keyword +"-------------------------------");
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpHost proxy = new HttpHost("112.250.107.37", 53281);

        HttpGet httpGet = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setConnectionRequestTimeout(3000)
                .build();
   //     httpGet.setConfig(requestConfig);

        httpGet.setHeader("Connection","keep-alive");
       // httpGet.addHeader(new BasicHeader("Cookie","t=9799cf0db1ce8117f2bd87929f396c38; thw=cn; enc=%2B3H8i3RPl1Zik0mXJf%2ByYZGXZFCEDWUaTS3NTIKzz49OzOK6CBsYVwoOc34tnRQNpuUrJ4i6k1Q6lvtigMtCNQ%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; _samesite_flag_=true; cookie2=1638c42ee942d27068be4490c369ac65; _tb_token_=e75534be363be; lLtC1_=1; cna=CeCREt3pVUcCAd9AQcqEQyOG; v=0; miid=160471411254026958; tk_trace=oTRxOWSBNwn9dPyorMJE%2FoPdY8zfvmw%2Fq5hp1FcNyAoqhAsWp1R1%2F8qus3FoDR8td0Qp3APXP%2FsRqbi4AskjVYmw%2FiPxBuQP91n0zL%2B3jiyUxrORyTqpnKNv1MycCE1wskzr7IOM8GT47c%2FYjGYclWFWczBDlX3%2FNyTu3u74yvaHs0IRmXVX4SO4mZ0UHf2p8mHwvLnQ8%2F%2BMrBCvaf%2FI2kjLp3FKrVaSjNDHfPBii2Nsg3V0JOJmkrIGunl2NIxmEKAYxv8sGa2as47Pv1Kh5mgmvoZd; sgcookie=ESzBlYFPQB91R8o86nn91; unb=1087242247; uc3=nk2=F5QqMn4HTg%3D%3D&vt3=F8dBxGZuEFNmIklGmFs%3D&id2=UoH39OV0g5scGg%3D%3D&lg2=URm48syIIVrSKA%3D%3D; csg=e0c3c4c0; lgc=tb_jade; cookie17=UoH39OV0g5scGg%3D%3D; dnk=tb_jade; skt=1a5cb92caad6906c; existShop=MTU4OTkwMzc5Nw%3D%3D; uc4=nk4=0%40FY5hDOvdfJRnAGSQuTXp%2FpqR&id4=0%40UOnoi869srkoiSwDzwSTG%2FmDHn8h; tracknick=tb_jade; _cc_=URm48syIZQ%3D%3D; _l_g_=Ug%3D%3D; sg=e7a; _nk_=tb_jade; cookie1=WvmBZL91gxsFKTjeSvTuLyKvuQTzcK3HSKeplffd7EI%3D; ucn=center; mt=ci=21_1; uc1=existShop=false&cookie14=UoTUM2nSSiFV3g%3D%3D&cookie16=UIHiLt3xCS3yM2h4eKHS9lpEOw%3D%3D&pas=0&cookie15=WqG3DMC9VAQiUQ%3D%3D&cookie21=UtASsssme%2BBq; isg=BMvLH3ojEVZIYE2HT-NFq4ASWmm1YN_iLQv58T3IqophXOu-xDXwMmm9MlSy_Dfa; l=eBMBKY4HvSRbyCKiBOfwFurza77OsIRAguPzaNbMiT5POHfp5hH1WZAJN5T9CnGNh6qpJ3l_zy1TBeYBc_C-nxvtQLBHiBkmn; tfstk=cuIdBuvC0RHp436eugUMPGFYpy2GZ0z9a2Om285777_KPtmRi8ScDjvzdKGpXRC.."));

        httpGet.addHeader(new BasicHeader("Cookie", "miid=1296267545453648768; t=b4d385e2145f596a67961e4dd08e9a8f; cna=pqwcFXxbJjACAXWIA7AFEfA8; thw=cn; tracknick=tb487881011; lgc=tb487881011; _cc_=UIHiLt3xSw%3D%3D; tg=0; enc=%2FTqA3gAexHOKU0cyPYbSWM1pGS8vgnlEK3EMnkYd2T%2BlB%2BJh18hxryREG48c%2BYmdk7yfvbSMCBDQExP23eUm3w%3D%3D; hng=CN%7Czh-CN%7CCNY%7C156; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; cookie2=19ef67fdfc3f433776e5e9cafaf6a8ea; v=0; _tb_token_=08b7e3e7e183; _m_h5_tk=62383241b06635c64b07942e50e47d9d_1562004576179; _m_h5_tk_enc=0465da475a8335f8fd8d9ef6bb280a71; unb=4235284520; sg=101; _l_g_=Ug%3D%3D; skt=c571ae590b7580cb; cookie1=AnQIvxj44XbyESoVNTVtwfJRB8W%2BbAPV%2BVZMWhAghjk%3D; csg=23f40375; uc3=vt3=F8dBy34cs3fc7ebsEqk%3D&id2=Vy67WD1MZomrsw%3D%3D&nk2=F5RBzeKtOazPVJc%3D&lg2=UtASsssmOIJ0bQ%3D%3D; existShop=MTU2MTk5NTE3MQ%3D%3D; dnk=tb487881011; _nk_=tb487881011; cookie17=Vy67WD1MZomrsw%3D%3D; mt=ci=21_1; uc1=cookie14=UoTaGdT0tHdY5w%3D%3D&lng=zh_CN&cookie16=VT5L2FSpNgq6fDudInPRgavC%2BQ%3D%3D&existShop=false&cookie21=VFC%2FuZ9aj3yE&tag=8&cookie15=UIHiLt3xD8xYTw%3D%3D&pas=0; whl=-1%260%260%261561995222497; isg=BHNzJqpkKgCWtOesccf13ZRUAnddACwkF8iwAyUQzxLJJJPGrXiXutG23hRvn19i; l=bBMxcfBPv539-OTkBOCanurza77OSIRYYuPzaNbMi_5K-6T_2qQOkAuQFF96Vj5Rs4YB4G2npwJ9-etkq"));

// 模拟浏览器浏览（user-agent的值可以通过浏览器浏览，查看发出请求的头文件获取）
        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
// 获取响应状态码

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            // 如果状态响应码为200，则获取html实体内容或者json文件
            if(statusCode == 200){
                String html = null;
                try {
                    html = EntityUtils.toString(entity, Consts.UTF_8);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 提取HTML得到商品信息结果
                Document doc = null;
                // doc获取整个页面的所有数据
                doc = Jsoup.parse(html);
                //输出doc可以看到所获取到的页面源代码
//      System.out.println(doc);
                // 通过浏览器查看商品页面的源代码，找到信息所在的div标签，再对其进行一步一步地解析
                Elements ulList = doc.select("div[class='view grid-nosku']");

                if(ulList.size() == 0){
                    ulList = doc.select("div[class='view ']");
                }
                Elements liList = ulList.select("div[class='product']");
                // 循环liList的数据（具体获取的数据值还得看doc的页面源代码来获取，可能稍有变动）
                if(liList.size() == 0){
                    System.exit(0);
                }
                for (Element item : liList) {
                    System.out.println("当前页码： " + pageValue);
                    // 商品ID
                    String id = item.select("div[class='product']").select("p[class='productStatus']").select("span[class='ww-light ww-small m_wangwang J_WangWang']").attr("data-item");
                    System.out.println("商品ID："+id);
                    // 商品名称
                    String name = item.select("p[class='productTitle']").select("a").attr("title");
                    System.out.println("商品名称："+name);
                    // 商品价格
                    String price = item.select("p[class='productPrice']").select("em").attr("title");
                    System.out.println("商品价格："+price);
                    // 商品网址
                    String goodsUrl = item.select("p[class='productTitle']").select("a").attr("href");
                    System.out.println("商品网址："+goodsUrl);
                    // 商品图片网址
                    String imgUrl = item.select("div[class='productImg-wrap']").select("a").select("img").attr("data-ks-lazyload");
                    System.out.println("商品图片网址："+imgUrl);
                    System.out.println("------------------------------------");

                    try {
                        collectGoods(goodsUrl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            } else {
                // 消耗掉实体
                EntityUtils.consume(response.getEntity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.close();
        }


    }
}
