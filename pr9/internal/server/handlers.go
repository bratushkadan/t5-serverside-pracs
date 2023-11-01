package server

import (
	"errors"
	"fmt"
	"log"
	"net/http"
	"os"
	"runtime"
	"sync"
	"time"

	"pr9/internal/crypto"

	"github.com/gin-gonic/gin"
)

var encrypter crypto.Encrypter

var ErrNoCookieName = errors.New("no cookieName provided")

func init() {
	encrypterSecretKey := os.Getenv("ENCRYPTER_SECRET_KEY")

	if encrypterSecretKey == "" {
		log.Fatalf("env 'ENCRYPTER_SECRET_KEY' has to be provided.")
	}

	encrypter = crypto.NewDefaultEncrypter(encrypterSecretKey)
}

func HandlePingRequest(c *gin.Context) {
	c.JSON(200, gin.H{
		"message":   "ok",
		"timestamp": time.Now().UnixMilli(),
	})
}

func HandleRequest(c *gin.Context) {
	cookieName := c.GetString("cookieName")
	if cookieName == "" {
		fmt.Println(ErrNoCookieName)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Server configuration error."})
		return
	}

	var request Request

	if err := c.ShouldBindJSON(&request); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	response := "Привет, " + request.Message + "!"

	encryptedResponse, err := encrypter.Encrypt([]byte(response))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to encrypt response"})
		return
	}
	if encryptedResponse == "" {
		c.JSON(
			http.StatusBadRequest,
			gin.H{"error": "Cookie data is corrupt."},
		)
		return
	}
	c.SetCookie(cookieName, encryptedResponse, 3600, "", "", false, true)

	c.JSON(http.StatusOK, gin.H{cookieName: response})
}

func GetResponseFromCookie(c *gin.Context) {
	cookieName := c.GetString("cookieName")
	if cookieName == "" {
		fmt.Println(ErrNoCookieName)
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Server configuration error."})
		return
	}

	encryptedResponse, err := c.Cookie(cookieName)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "Response cookie not found"})
		return
	}

	decryptedResponse, err := encrypter.Decrypt([]byte(encryptedResponse))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "Failed to decrypt response"})
		return
	}
	if len(decryptedResponse) == 0 {
		c.JSON(
			http.StatusBadRequest,
			gin.H{"error": "Cookie data is corrupt."},
		)
		return
	}

	c.JSON(http.StatusOK, gin.H{cookieName: string(decryptedResponse)})
}

func LoopHandler(c *gin.Context) {
	s := 0
	var i int
	for i = 0; i < 100; i++ {
		time.Sleep(15 * time.Millisecond)
		s += i
	}

	c.JSON(http.StatusOK, gin.H{"data": s})
}
func LoopParallelHandler(c *gin.Context) {

	numCpu := runtime.NumCPU()

	wg := &sync.WaitGroup{}
	results := make(chan int, numCpu)

	for n := 0; n < numCpu; n++ {
		wg.Add(1)
		go func(r chan int, k, n int) {
			defer wg.Done()

			fmt.Println(k)

			s := 0
			var i int
			for i = n; i < 100; i += n {
				time.Sleep(15 * time.Millisecond)
				s += i
			}

			r <- s

			fmt.Println("Goroutine", s)
		}(results, n, numCpu)
	}

	go func(wg *sync.WaitGroup) {
		wg.Wait()
		close(results)
	}(wg)

	s := 0
	for v := range results {
		s += v
	}
	c.JSON(http.StatusOK, gin.H{"data": s})
}
