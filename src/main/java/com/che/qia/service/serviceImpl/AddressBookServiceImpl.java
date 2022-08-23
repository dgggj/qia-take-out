package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.AddressBook;
import com.che.qia.mapper.AddressBookMapper;
import com.che.qia.service.AddressBookService;
import org.apache.tomcat.jni.Address;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description AddressBookServiceImpl
 * #Date: 2022/8/22 13:14
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
