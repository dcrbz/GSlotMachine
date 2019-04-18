package com.guillaumevdn.gslotmachine;

import com.guillaumevdn.gcore.lib.Perm;

public class GSMPerm {

	public static final Perm GSLOTMACHINE_ROOT = new Perm(null, "gslotmachine.*");
	public static final Perm GSLOTMACHINE_ADMIN = new Perm(GSLOTMACHINE_ROOT, "gslotmachine.admin");

}
