package com.worldcheck.atlas.vo.masters;

import com.worldcheck.atlas.exception.CMSException;
import com.worldcheck.atlas.logging.producer.ILogProducer;
import com.worldcheck.atlas.logging.producer.LogProducerImpl;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserCountryProfileVO {
	private ILogProducer logger = LogProducerImpl.getLogger("com.worldcheck.atlas.vo.masters.UserCountryProfileVO");
	public String cntryCode;
	public int engMediaList;
	public int locLangList;
	public String countryId;
	private long userMasterId;
	private int profileCountryId;

	public int totalHashCode() {
		String yourString = this.engMediaList + ":" + this.locLangList + ":" + this.profileCountryId + ":"
				+ this.userMasterId;

		try {
			byte[] bytesOfMessage = yourString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public int localLangHashCode() {
		String yourString = this.locLangList + ":" + this.profileCountryId + ":" + this.userMasterId;

		try {
			byte[] bytesOfMessage = yourString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public int englishHashCode() {
		String yourString = this.engMediaList + ":" + this.profileCountryId + ":" + this.userMasterId;

		try {
			byte[] bytesOfMessage = yourString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public int countryHashCode() {
		String yourString = this.profileCountryId + ":" + this.userMasterId;

		try {
			byte[] bytesOfMessage = yourString.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			String newString = new String(thedigest);
			return newString.hashCode();
		} catch (UnsupportedEncodingException var6) {
			new CMSException(this.logger, var6);
			return 0;
		} catch (NoSuchAlgorithmException var7) {
			new CMSException(this.logger, var7);
			return 0;
		}
	}

	public String getCountryId() {
		return this.countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	public String getCntryCode() {
		return this.cntryCode;
	}

	public void setCntryCode(String cntryCode) {
		this.cntryCode = cntryCode;
	}

	public int getEngMediaList() {
		return this.engMediaList;
	}

	public void setEngMediaList(int engMediaList) {
		this.engMediaList = engMediaList;
	}

	public int getLocLangList() {
		return this.locLangList;
	}

	public void setLocLangList(int locLangList) {
		this.locLangList = locLangList;
	}

	public long getUserMasterId() {
		return this.userMasterId;
	}

	public void setUserMasterId(long userMasterId) {
		this.userMasterId = userMasterId;
	}

	public int getProfileCountryId() {
		return this.profileCountryId;
	}

	public void setProfileCountryId(int profileCountryId) {
		this.profileCountryId = profileCountryId;
	}
}