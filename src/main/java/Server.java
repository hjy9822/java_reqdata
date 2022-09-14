import org.apache.commons.io.IOUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends AbstractJavaSamplerClient  {
    @Override
    public Arguments getDefaultParameters() {
        Arguments args = new Arguments();
        args.addArgument("Service IP", "localhost");
        args.addArgument("Service PORT", "1228");
        args.addArgument("Service Message", "No message");
        //return super.getDefaultParameters();
        return args;
    }


    static boolean valCheck1 = false;
    static boolean valCheck2 = false;

    @Override
    public void setupTest(JavaSamplerContext context) {
        // TODO Auto-generated method stub
        //super.setupTest(context);
        String port = context.getParameter("Service PORT");
        System.out.println(port);
        String ip = context.getParameter("Service IP");
        System.out.println(ip);
        String message = context.getParameter("Service Message");


        if(port.equals("1228")) {
            System.out.println("it's right! (PORT)");
            valCheck1 = true;
        }
        else if(!port.equals("1228")) {
            System.out.println("It's wrong! (PORT)");
        }

        if(ip.equals("localhost")) {
            System.out.println("it's right! (IP)");
            valCheck2 = true;
        }
        else if(!ip.equals("localhost")) {
            System.out.println("It's wrong! (IP)");
        }

        //System.out.println("valCheck1"+valCheck1);
        //System.out.println("valCheck2"+valCheck2);

        if( ! valCheck1 == true &&  ! valCheck2 == true){
           //System.out.println("valcheck false");
        }
    }

    final static int SERVER_PORT = 1228;
    final static String MESSAGE_TO_SERVER = "Hello, Client";

    public SampleResult runTest(JavaSamplerContext arg0) {
        JMeterVariables vars = JMeterContextService.getContext().getVariables();
        System.out.println( "runTest() :: " +  arg0.getParameter("Service PORT") );
        SampleResult sampleResult = new SampleResult();

        try {

            if( valCheck1 == true &&  valCheck2 == true){
                ServerSocket serverSocket = null;

                try {
                    serverSocket = new ServerSocket(SERVER_PORT);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    while (true) {
                        System.out.println("\nsocket 연결 대기");
                        Socket socket = serverSocket.accept();
                        System.out.println("host : " + socket.getInetAddress() + " | 통신 연결 성공");

                        /**    Server에서 보낸 값을 받기 위한 통로 */
                        InputStream is = socket.getInputStream();
                        /**    Server에서 Client로 보내기 위한 통로 */
                        OutputStream os = socket.getOutputStream();

                        byte[] data = new byte[600];
                        int n = is.read(data);
                        final String messageFromClient = new String(data, 0, n);

                        System.out.println("Client에서 넘겨받은 메시지 : "+messageFromClient);

                        os.write(MESSAGE_TO_SERVER.getBytes());
                        os.flush();

                        is.close();
                        os.close();
                        socket.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                sampleResult.setSuccessful(true);
                sampleResult.setResponseCodeOK();
                sampleResult.setResponseMessageOK();
            }else{
                sampleResult.setSuccessful(false);
            }

        }catch (Exception e){
            sampleResult.setSuccessful(false);
        }finally {
            sampleResult.sampleEnd();
        }
        return sampleResult;

    }


    @Override
    public void teardownTest(JavaSamplerContext context) {
        System.out.println("It's all done~~ ");
        super.teardownTest(context);
    }

}

class SocketRun implements Runnable {

    private Socket socket = null;

    SocketRun( Socket socket ){
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}



