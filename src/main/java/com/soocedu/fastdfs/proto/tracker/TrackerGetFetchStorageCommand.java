package com.soocedu.fastdfs.proto.tracker;

import com.soocedu.fastdfs.domain.StorageNodeInfo;
import com.soocedu.fastdfs.proto.AbstractFdfsCommand;
import com.soocedu.fastdfs.proto.FdfsResponse;
import com.soocedu.fastdfs.proto.tracker.internal.TrackerGetFetchStorageRequest;

/**
 * 获取源服务器
 * 
 * @author tobato
 *
 */
public class TrackerGetFetchStorageCommand extends AbstractFdfsCommand<StorageNodeInfo> {

    public TrackerGetFetchStorageCommand(String groupName, String path, boolean toUpdate) {
        super.request = new TrackerGetFetchStorageRequest(groupName, path, toUpdate);
        super.response = new FdfsResponse<StorageNodeInfo>() {
            // default response
        };
    }

}
