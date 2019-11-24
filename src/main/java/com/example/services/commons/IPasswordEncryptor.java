package com.example.services.commons;

/**
 * Данный интерфейс требуется для того чтобы изолировать
 * "security" библиотеки спринга от сервисов.
 */
@FunctionalInterface
public interface IPasswordEncryptor {

  String encrypt(String rawPassword);
}

