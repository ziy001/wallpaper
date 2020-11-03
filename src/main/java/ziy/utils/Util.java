package ziy.utils;

import ziy.utils.dll.User32;
import ziy.utils.request.Request;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Util {
    /**
     * 设置桌面背景
     * @return
     */
    private static boolean setDeskWallpaper0(String absolutePath) {
        //方法一：修改注册表
//        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
//                "Control Panel\\Desktop", "Wallpaper", absolutePath);
//        //WallpaperStyle = 10 (Fill), 6 (Fit), 2 (Stretch), 0 (Tile), 0 (Center)
//        //For windows XP, change to 0
//        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
//                "Control Panel\\Desktop", "WallpaperStyle", "10"); //fill
//        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER,
//                "Control Panel\\Desktop", "TileWallpaper", "0");   // no tiling
        //方法二：使用函数
        // refresh the desktop using User32.SystemParametersInfo(), so avoiding an OS reboot
        //参数通过Windows API手册查询
        int SPI_SETDESKWALLPAPER = 0x14;
        int SPIF_UPDATEINIFILE = 0x01;
        int SPIF_SENDWININICHANGE = 0x02;

        // User32.System  通过User32里面加载windows的User32.dll文件并调用里面的SystemPapametersInfo函数进行修改
        return User32.INSTANCE.SystemParametersInfoA(SPI_SETDESKWALLPAPER, 0,
                absolutePath, SPIF_UPDATEINIFILE | SPIF_SENDWININICHANGE );
    };

    /**
     * 构建文件的Path对象
     * @param parentPath
     * @return
     */
    private static Path createPath(String parentPath) throws IOException {
        String fileName = ""+System.currentTimeMillis()+".jpg";
        Path filePath = Paths.get(parentPath, fileName);
        //检查文件并保证已创建
        if(!Files.exists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }
        if(!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
        return filePath;
    }

    /**
     * 对图片链接进行校验
     * @param imgUrl
     * @return
     */
    private static boolean checkURL(String imgUrl) {
        try {
            URI.create(imgUrl).toURL().openConnection().setConnectTimeout(5);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    /**
     * 用于手动设置桌面背景
     * @param downloadPath 壁纸保存路径
     * @param imgUrl 图片链接
     * @return true；设置成功 false:设置失败
     */
    public static boolean setWallpaper(String downloadPath, String imgUrl) {
        if (!checkURL(imgUrl)) {
            return false;
        }
        //构建文件的Path
        Path filePath = null;
        try {
            filePath = createPath(downloadPath);
        } catch (IOException e) {
            return false;
        }
        //进行网络下载
        download(filePath, imgUrl);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return setDeskWallpaper0(filePath.toFile().getAbsolutePath());
    }

    /**
     * 下载图片
     * @param downloadPath 图片下载路径
     * @param imgUrl 图片链接
     * @return true:下载成功 false:下载失败
     */
    public static boolean downloadImg(String downloadPath, String imgUrl) {
        if (!checkURL(imgUrl)) {
            return false;
        }
        //构建文件的path
        Path path = null;
        try {
            path = createPath(downloadPath);
        } catch (IOException e) {
            return false;
        }
        //调用下载
        return download(path, imgUrl);

    }
    /**
     * 对指定文件进行下载
     * @param filePath 代表文件的Path对象
     * @param imgUrl 图片链接
     * @return
     */
    private static boolean download(Path filePath, String imgUrl) {
        try {
            Request.download(filePath, imgUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
