/**
 * Description:
 * Date: 19-1-22 上午9:27
 *
 * @author ujued
 */
package com.codingapi.txlcn.client.corelog;

// TC's core log used h2 database. don't care the special SQL statement.

// What's the core log?
// 1. aspect log：Transaction Unit 's execute context.
// 2. TXC undo log: reverse sql info of TXC transaction type.