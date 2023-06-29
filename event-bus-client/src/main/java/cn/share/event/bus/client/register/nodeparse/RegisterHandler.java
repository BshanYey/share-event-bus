package cn.share.event.bus.client.register.nodeparse;

import cn.share.event.bus.client.entity.param.Node;
import cn.share.event.bus.client.register.store.DataStorage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author yangjie.deng@resico.cn
 * @date 2023-06-29 09:12:54
 * @describe class responsibility
 */
@Slf4j
public class RegisterHandler {

    public static void register() {
        try {
            int nodeCount = RegisterNodeProcessor.alreadyRegisterCount();

            Map<String, List<Node>> registerNodes = DataStorage.allRegisterNodes();

            CountDownLatch downLatch = new CountDownLatch(nodeCount);

            registerNodes.keySet()
                    .parallelStream()
                    .forEach(key -> doRegister(downLatch, registerNodes.get(key)));

            downLatch.await();
        } catch (InterruptedException e) {

        }
    }

    private static void doRegister(CountDownLatch downLatch, List<Node> nodes) {

    }
}
