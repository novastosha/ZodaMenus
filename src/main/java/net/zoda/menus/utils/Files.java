package net.zoda.menus.utils;

import net.zoda.menus.plugin.ZodaMenusPlugin;

import java.io.*;

public enum Files {
    CONFIG(new File(ZodaMenusPlugin.getInstance().getDataFolder(),"config.yml"),true,true){
        @Override
        public void onCreate() {
            try {
                FileWriter fileWriter = new FileWriter(getFile());

                BufferedReader reader = new BufferedReader(new InputStreamReader(ZodaMenusPlugin.getInstance().getResource("config.yml")));

                reader.lines().forEach(s -> {
                    try {
                        fileWriter.write(s+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                reader.close();
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    },
    PLUGINS(new File("./plugins"),false,false){
        @Override
        public void onCreate() {

        }
    },
    MENUS_FOLDER(new File(ZodaMenusPlugin.getInstance().getDataFolder(),"menus"),true,false) {
        @Override
        public void onCreate() {

        }
    }
    ;

    private final File file;
    private final boolean isFile;
    private final boolean create;

    Files(File file, boolean create, boolean isFile) {
        this.file = file;
        this.create = create;
        this.isFile = isFile;
    }

    public abstract void onCreate();

    public boolean isFile() {
        return isFile;
    }

    public boolean isToCreate() {
        return create;
    }

    public File getFile() {
        return file;
    }
}
