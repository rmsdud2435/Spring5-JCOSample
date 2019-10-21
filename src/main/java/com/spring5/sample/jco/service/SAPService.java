package com.spring5.sample.jco.service;

import com.spring5.sample.jco.exception.SapException;
import com.spring5.sample.jco.mapper.ISapMapper;

public abstract interface SAPService
{
  public abstract Object execute(String paramString, Object paramObject, ISapMapper paramISapMapper)throws SapException;
}
