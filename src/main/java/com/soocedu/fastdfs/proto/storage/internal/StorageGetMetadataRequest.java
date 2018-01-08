package com.soocedu.fastdfs.proto.storage.internal;


import com.soocedu.fastdfs.proto.CmdConstants;
import com.soocedu.fastdfs.proto.FdfsRequest;
import com.soocedu.fastdfs.proto.OtherConstants;
import com.soocedu.fastdfs.proto.ProtoHead;
import com.soocedu.fastdfs.proto.mapper.DynamicFieldType;
import com.soocedu.fastdfs.proto.mapper.FdfsColumn;

/**
 * 查询文件信息命令
 * 
 * @author tobato
 *
 */
public class StorageGetMetadataRequest extends FdfsRequest {

    /** 组名 */
    @FdfsColumn(index = 0, max = OtherConstants.FDFS_GROUP_NAME_MAX_LEN)
    private String groupName;
    /** 路径名 */
    @FdfsColumn(index = 1, dynamicField = DynamicFieldType.allRestByte)
    private String path;

    /**
     * 删除文件命令
     * 
     * @param groupName
     * @param path
     */
    public StorageGetMetadataRequest(String groupName, String path) {
        super();
        this.groupName = groupName;
        this.path = path;
        this.head = new ProtoHead(CmdConstants.STORAGE_PROTO_CMD_GET_METADATA);
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
