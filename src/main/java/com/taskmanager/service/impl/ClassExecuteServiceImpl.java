package com.taskmanager.service.impl;


import com.jcl.jcltry.Executor;
import com.taskmanager.model.Task;
import com.taskmanager.service.api.ClassExecuteService;
import com.taskmanager.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;
import org.xeustechnologies.jcl.context.DefaultContextLoader;
import org.xeustechnologies.jcl.proxy.CglibProxyProvider;
import org.xeustechnologies.jcl.proxy.ProxyProviderFactory;

import javax.sql.rowset.serial.SerialException;
import java.io.ByteArrayInputStream;

/**
 * Implementation for {@link ClassExecuteService}
 */
@Service
public class ClassExecuteServiceImpl implements ClassExecuteService {

    private static final Logger log = LoggerFactory.getLogger(ClassExecuteServiceImpl.class);

    @Value("${app.mainClassName:com.jcl.jcltry.ExecutorImpl}")
    private String mainClassName;

    @Override
    public void execute(Task task) {
        try {
            JarClassLoader jcl = new JarClassLoader();
            jcl.add(new ByteArrayInputStream(task.getData()));

            DefaultContextLoader context = new DefaultContextLoader(jcl);
            context.loadContext();

            ProxyProviderFactory.setDefaultProxyProvider(new CglibProxyProvider());

            //Create a factory of castable objects/proxies
            JclObjectFactory factory = JclObjectFactory.getInstance(true);

            //Create object of loaded class
            Executor executor = (Executor) factory.create(jcl, mainClassName);

            String[] args = task.getProperty().split(" ");
            task.setResult(executor.execute(args));
            log.info("Execute task id = {} with property = {} and result = {}", task.getId(), args, task.getResult());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }
}
