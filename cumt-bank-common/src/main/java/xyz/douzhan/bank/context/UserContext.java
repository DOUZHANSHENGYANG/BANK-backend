package xyz.douzhan.bank.context;



/**
 * 一些声明信息
 * Description:用户上下文信息
 * date: 2023/12/8 19:38
 *
 * @author 斗战圣洋
 * @since JDK 17
 */
public class UserContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setContext(Long id) {
        threadLocal.set(id);
    }

    public static Long getContext() {
        return threadLocal.get();
    }

    public static void removeContext() {
        threadLocal.remove();
    }
}
