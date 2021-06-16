package edu.uw.tjb;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import test.AccountTest;
import test.DaoTest;
import test.AccountManagerTest;
import test.PrivateMessageCodecTest;


@RunWith(JUnitPlatform.class)
@SelectClasses({PrivateMessageCodecTest.class})
class TestSuite {}
