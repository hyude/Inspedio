package com.inspedio.samples;


import com.inspedio.enums.ScreenOrientation;
import com.inspedio.system.InsMain;
import com.inspedio.system.core.InsLoader;
import com.inspedio.system.helper.InsSave;

public class MainSample extends InsMain{

	protected void init() {
		this.init(new SampleButtonState(), new InsLoader(), new InsSave("Runner"), ScreenOrientation.PORTRAIT);
	}

	

}
