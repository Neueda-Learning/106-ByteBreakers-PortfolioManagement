import axios from 'axios'
import { useEffect, useState } from 'react'

export function useInvestments() {
    const [investments, setInvestments] = useState([] as any[])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        const fetchInvestments = async () => {
            try {
                const response = await axios.get('http://localhost:8081/userInvestments/')
                setInvestments(Array.isArray(response.data) ? response.data : [])
            } catch {
                setInvestments([])
            } finally {
                setLoading(false)
            }
        }

        void fetchInvestments()
    }, [])

    return {
        investments,
        loading,
    }
}
