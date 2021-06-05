package edu.uw.tjb;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import test.AccountTest;
import test.DaoTest;
import test.AccountManagerTest;

//@SelectClasses({AccountTest.class, DaoTest.class, AccountManagerTest.class})
//@SelectClasses({DaoTest.class, AccountManagerTest.class})

@RunWith(JUnitPlatform.class)
@SelectClasses({AccountTest.class, DaoTest.class, AccountManagerTest.class})
class TestSuite {}
