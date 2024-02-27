import com.example.demo.Transaction
import com.example.demo.TransactionTypes
import com.example.demo.Wallet
import com.example.demo.Result

fun main() {
    val transaction = Transaction.create(100f, TransactionTypes.PIX)

    val wallet = Wallet.create(10f)

    when (val result = wallet.payTransaction(transaction)) {
        is Result.InsufficientBalance -> println("${result.name}: ${result.errorMessage}")
        is Result.DuplicatedTransaction -> println("${result.name}: ${result.errorMessage}")
        is Result.Success -> println("Payment made successfully!")
    }

    println("${transaction.id}: ${transaction.status}")
    println(wallet.transactions.toString())

    wallet.addBalance(100f)

    when (val result = wallet.payTransaction(transaction)) {
        is Result.InsufficientBalance -> println("${result.name}: ${result.errorMessage}")
        is Result.DuplicatedTransaction -> println("${result.name}: ${result.errorMessage}")
        is Result.Success -> println("Payment made successfully!")
    }

    println("${transaction.id}: ${transaction.status}")
    println(wallet.transactions.toString())

    val transaction2 = Transaction.create(100f, TransactionTypes.PIX)

    when (val result = wallet.payTransaction(transaction2)) {
        is Result.InsufficientBalance -> println("${result.name}: ${result.errorMessage}")
        is Result.DuplicatedTransaction -> println("${result.name}: ${result.errorMessage}")
        is Result.Success -> println("Payment made successfully!")
    }

    println("${transaction2.id}: ${transaction2.status}")
    println(wallet.transactions.toString())
}