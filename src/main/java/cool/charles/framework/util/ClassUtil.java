package cool.charles.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@Slf4j
public final class ClassUtil {
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> claz;

        try{
            claz = Class.forName(className, isInitialized, getClassLoader());
        }catch(ClassNotFoundException e) {
            log.error("load class failure, {}", e);
            throw new RuntimeException(e);
        }

        return claz;
    }

    public static Set<Class<?>> getClassSet(String packageName) {
        Set<Class<?>> clazSet = new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if(url != null) {
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")) {
                        String packagePath = url.getPath().replace("%20"," ");
                        addClass(clazSet, packagePath, packageName);
                    } else if(protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if(jarURLConnection !=null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile != null) {
                                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                                while(jarEntryEnumeration.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if(jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(clazSet,className);
                                    }
                                }
                            }
                        }
                    }

                }
            }

        }catch (Exception e) {
            log.error("class set load failure {}", e);
            throw new RuntimeException(e);
        }

        return clazSet;

    }

    private static void addClass(Set<Class<?>> classSet, final String packagePath, final String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".calss")) || pathname.isDirectory();
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if(file.isFile()) {
                String clazName = fileName.substring(0,fileName.lastIndexOf("."));
                if(StringUtil.isNotEmpty(packageName)) {
                    clazName = packageName + "." + clazName;
                }

                doAddClass(classSet, clazName);

            } else {
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }

                addClass(classSet,subPackagePath,subPackageName);

            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> claz = loadClass(className, false);
        classSet.add(claz);
    }


}
