package com.noar.dbist.util;

import com.noar.dbist.metadata.Column;

public interface ValueGenerator {
	public Object generate(Object data, Column column) throws Exception;
}
