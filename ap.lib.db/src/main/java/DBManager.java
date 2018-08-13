import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

import java.io.File;

public class DBManager {
    //TODO 路径需修改为自己的存放路径
    protected static final String sDir = "/Users/lugang/Documents/xuexi_Space/VirtualApkProgram/app/src/main/java";
//    protected static final String sDir = "./app/src/main/java-gen";

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.virtual.xuancao.virtualapkprogram.db.bean");
        schema.setDefaultJavaPackageDao("com.virtual.xuancao.virtualapkprogram.db.dao");
        addPersonInfo(schema);
        File file = new File(sDir);
        if (!file.exists()){
            file.mkdir();
        }
        new org.greenrobot.greendao.generator.DaoGenerator().generateAll(schema, sDir);
    }


    /***
     * 个人信息
     */
    private static void addPersonInfo(Schema schema) {
        Entity personInfo = schema.addEntity("UserInfoDB");
        personInfo.addStringProperty("user_id");
        personInfo.addStringProperty("nick_name");
        personInfo.addStringProperty("birthday");
        personInfo.addStringProperty("avatar");
        personInfo.addStringProperty("gender");
        personInfo.addStringProperty("mobile");
    }
}
