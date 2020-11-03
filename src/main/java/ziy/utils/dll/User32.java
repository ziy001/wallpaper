package ziy.utils.dll;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

/**
 * @author ziy
 * @version 1.0
 * @date 19:24 2020/11/1
 * @description TODO:
 * @className
 */
public interface User32 extends StdCallLibrary {
    User32 INSTANCE = (User32) Native.loadLibrary("user32", User32.class);
    boolean SystemParametersInfoA(int uiAction, int uiParam, String fnm, int fWinIni);
}
