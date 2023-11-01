package crypto

import (
	"fmt"
	"log"

	"github.com/golang-module/dongle"
)

type Encrypter interface {
	Encrypt(data []byte) (string, error)
	Decrypt(data []byte) ([]byte, error)
}

type DefaultEncrypter struct {
	cipher *dongle.Cipher
	Encrypter
}

func (e DefaultEncrypter) Encrypt(data []byte) (string, error) {
	fmt.Println(string(data), dongle.Encrypt.FromBytes(data).ByAes(e.cipher).ToHexString())
	return dongle.Encrypt.FromBytes(data).ByAes(e.cipher).ToHexString(), nil
}
func (e DefaultEncrypter) Decrypt(data []byte) ([]byte, error) {
	return dongle.Decrypt.FromHexBytes(data).ByAes(e.cipher).ToBytes(), nil
}

func NewDefaultEncrypter(secretKey string) Encrypter {
	fmt.Println("NewDefaultEncrypter", secretKey)

	secKeyByteLen := len([]byte(secretKey))
	if secKeyByteLen%16 != 0 && secKeyByteLen != 24 {
		log.Fatalf("encrypter secretKey must be 16/24/32 bytes long")
	}

	cipher := dongle.NewCipher()
	cipher.SetMode(dongle.CBC)
	cipher.SetKey(secretKey)
	cipher.SetIV("0123456789abcdef")

	return &DefaultEncrypter{
		cipher: cipher,
	}
}
