package lishui.demo;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;


/**
 * @author lishui.lin
 * Created it on 2021/5/12
 */
public final class JavaUnitTest {

    @Test
    public void testObj() {
        String a = new String("Hello world");
        String b = new String("Hello world");
        System.out.println("a=" + a + ", b=" + b);
        Assert.assertEquals(a, b);
        int integer = new Integer(2);
        Assert.assertEquals(1 + 1, integer);
    }

    @Test
    public void testClass() {
        Class<InnerTask> innerClazz = InnerTask.class;
        Class clazz = innerClazz.getEnclosingClass();
        if (clazz != null) {
            println("InnerTask parent simple name=" + clazz.getSimpleName());
        }

        int mod = innerClazz.getModifiers();
        println("InnerTask modifiers=" + mod);

        Type superClassType = innerClazz.getGenericSuperclass();
        println("InnerTask super class name="+superClassType.getTypeName());

        // 获取InnerTask定义所在的类，因为InnerTask是定义在JavaUnitTest，故返回JavaUnitTest类对象
        // 而JavaUnitTest定义在独立的文件上，所以declaring class为null
        Class declaringClass = innerClazz.getDeclaringClass();
        if (declaringClass != null) {
            println("InnerTask declaring class="+declaringClass.getSimpleName());
        }

        Field[] fields = innerClazz.getDeclaredFields();
        for (Field field : fields) {
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                println("parameterizedType raw type="+parameterizedType.getRawType().getTypeName());
                Type[] paramsType = parameterizedType.getActualTypeArguments();
                for (Type paramType : paramsType) {
                    println("parameterizedType paramType name="+paramType.getTypeName());
                }
            }
            println("field name=" + field.getName()
                    + ", type=" + field.getGenericType().getTypeName()
                    + "\nfield generic string=" + field.toGenericString());

        }
    }

    private static class InnerTask implements Runnable {

        private final List<String> mContent = new ArrayList<>();
        @Override
        public void run() {
            System.out.println("InnerTask running...");
        }
    }

    private void println(String x) {
        System.out.println(x);
    }

}
