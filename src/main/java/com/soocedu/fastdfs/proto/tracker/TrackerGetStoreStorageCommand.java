package com.soocedu.fastdfs.proto.tracker;


import com.soocedu.fastdfs.domain.StorageNode;
import com.soocedu.fastdfs.proto.AbstractFdfsCommand;
import com.soocedu.fastdfs.proto.FdfsResponse;
import com.soocedu.fastdfs.proto.tracker.internal.TrackerGetStoreStorageRequest;
import com.soocedu.fastdfs.proto.tracker.internal.TrackerGetStoreStorageWithGroupRequest;

/**
 * 获取存储节点命令
 *
 * @author tobato
 *
 */
public class TrackerGetStoreStorageCommand extends AbstractFdfsCommand<StorageNode> {

    public TrackerGetStoreStorageCommand(String groupName) {
        super.request = new TrackerGetStoreStorageWithGroupRequest(groupName);
        super.response = new FdfsResponse<StorageNode>() {
            // default response
        };
    }

    public TrackerGetStoreStorageCommand() {
        super.request = new TrackerGetStoreStorageRequest();
        super.response = new FdfsResponse<StorageNode>() {
            // default response
        };
    }

}
