# Bloomfilter-store
Small scala library for bloom filters with Redis as backend.
Bloom filters can be unlimitied huge.
This library offers two simple methods
* def put(element: T): Future[Unit] -- put element in bloom filter
* def mightContain(element: T): Future[Boolean] - check if element is in bloom filter(with predefined probability)
Actual bloom filter is stored in Redis.
