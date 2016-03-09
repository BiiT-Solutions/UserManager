package com.biit.usermanager.entity;

import com.biit.utils.pool.PoolElement;

public interface IElement<Id> extends PoolElement<Id> {

	Id getId();
}
