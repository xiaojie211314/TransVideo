package com.soocedu.fastdfs.proto.tracker;

import com.soocedu.fastdfs.domain.GroupState;
import com.soocedu.fastdfs.proto.AbstractFdfsCommand;
import com.soocedu.fastdfs.proto.tracker.internal.TrackerListGroupsRequest;
import com.soocedu.fastdfs.proto.tracker.internal.TrackerListGroupsResponse;

import java.util.List;

/**
 * 列出组命令
 * 
 * @author tobato
 *
 */
public class TrackerListGroupsCommand extends AbstractFdfsCommand<List<GroupState>> {

    public TrackerListGroupsCommand() {
        super.request = new TrackerListGroupsRequest();
        super.response = new TrackerListGroupsResponse();
    }

}
